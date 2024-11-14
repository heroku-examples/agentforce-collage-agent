Extending the Coral Cloud Agent with Heroku
===========================================

This demo extends the popular Coral Cloud demo by extending the Coral Cloud Agent with the ability to dynamically generate a custom collage of the guests stay. You can watch a short demo video [here](https://www.youtube.com/watch?v=yd97A9GLFUA).


![alt text](downloads/test.png "Collage")

Deploy Instructions
----------------------------
- Deploy the [Coral Cloud sample](https://developer.salesforce.com/sample-apps), including the Service Cloud extension that includes setting up the Experience Cloud site and validate that you can see the Coral Cloud website and access the agent by booking an experience
- This sample stores generated images for future download in an AWS S3 bucket. Ensure you have created an AWS S3 bucket and setup a policy to permit read write access. The default bucket name in this sample is `coralcloudcollagefiles`
- Deploy as you would any [Heroku Java application](https://devcenter.heroku.com/articles/getting-started-with-java) using `git push heroku main`
- Set environment variables `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` locally and/or for Heroku via `heroku config:set` 
- Configure [Heroku Connect](https://www.heroku.com/connect) to the Salesforce Org you are using for the Coral Cloud sample (the free demo plan will work just fine). Configure mappings to the **Booking**, **Contact**, **Experience** and **Session** objects (all fields).
- Lastly configure a new **Agentforce Action** and add it to the **Coral Cloud Agent** (see instructions below)

Configuring the Agentforce Action
---------------------------------
- 
