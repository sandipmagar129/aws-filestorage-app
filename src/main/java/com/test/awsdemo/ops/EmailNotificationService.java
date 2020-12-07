package com.test.awsdemo.ops;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.CreateTemplateRequest;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.GetTemplateRequest;
import com.amazonaws.services.simpleemail.model.GetTemplateResult;
import com.amazonaws.services.simpleemail.model.MessageTag;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.Template;

@Service
public class EmailNotificationService {

	@Autowired
	AmazonSimpleEmailService sesClient;
	
	public GetTemplateResult createTemplate() {
		String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
			      + "<p>This email was sent with {{sourceapp}}"
			      + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>" 
			      + "AWS SDK for Java</a>";
		String TEXTBODY = "This email was sent through Amazon SES "
			      + "using the AWS SDK for Java. {{sourceapp}}";
		Template t = new Template();
		t.setSubjectPart("TEST EMAIL {{sourceapp}}");
		t.setTemplateName("test1");
		t.setHtmlPart(HTMLBODY);
		t.setTextPart(TEXTBODY); // If email client does not support html, text will be used
		
		CreateTemplateRequest ctr = new CreateTemplateRequest();
		ctr.setTemplate(t);
		sesClient.createTemplate(ctr);
		
		GetTemplateRequest gtr = new GetTemplateRequest();
		gtr.setTemplateName("test1");
		return sesClient.getTemplate(gtr);
	}
	
	public String sendEmail() {
		JSONObject templateData = new JSONObject();
		templateData.put("sourceapp", "awsdemo");

		
		List<MessageTag> tags = new ArrayList<MessageTag>();
		MessageTag tag = new MessageTag();
		tag.setName("usage");
		tag.setValue("test");
		tags.add(tag);
		
		List<String> toAddresses = new ArrayList<String>();
		toAddresses.add("precproject@gmail.com");
		
		Destination dest = new Destination();
		dest.setToAddresses(toAddresses);
		
		SendTemplatedEmailRequest ster = new SendTemplatedEmailRequest();
		ster.setTemplate("test1");
		ster.setTags(tags);
		ster.setDestination(dest);
		ster.setSource("precproject@gmail.com");
		ster.setTemplateData(templateData.toString());
		
		sesClient.sendTemplatedEmail(ster);
		return sesClient.getSendStatistics().getSendDataPoints().toString();
	}
}
