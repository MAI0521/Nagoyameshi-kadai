package com.example.nagoyameshi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.SubscriptionUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	// セッションを作成し、Stripeに必要な情報を返す
	
	@Value("${stripe.api-key}")
    private String stripeApiKey;
	
	 @PostConstruct
     private void init() {
         // Stripeのシークレットキーを設定する
         Stripe.apiKey = stripeApiKey;
     }
	
	private final UserService userService;
    
    public StripeService(UserService userService) {
        this.userService = userService;
    }    
	
    public String createStripeSession(String username, HttpServletRequest httpServletRequest) {
    	Stripe.apiKey = stripeApiKey;
    	String requestUrl = httpServletRequest.getRequestURL().toString();
        String priceId = "price_1Q9M3bG1Q8EB8XUacUbkFtmb"; 
        
        SessionCreateParams sessionCreateParams = new SessionCreateParams.Builder()
        		  .setSuccessUrl(requestUrl.replace("/create-checkout-session", "") + "/upgraded?session_id={CHECKOUT_SESSION_ID}")
        		  .setCancelUrl(requestUrl.replace("/create-checkout-session", ""))
        		  .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
        		  .addLineItem(new SessionCreateParams.LineItem.Builder()
        		    // For metered billing, do not pass quantity
        		    .setQuantity(1L)
        		    .setPrice(priceId)
        		    .build()
        		  )
        		  .setCustomerEmail(getCurrentUserName()) 
        		  .build();
        
        try {
            Session session = Session.create(sessionCreateParams);
            System.out.println("Stripe session created with ID: " + session.getId());
            return session.getId();  // Return the session ID
        } catch (StripeException e) {
            e.printStackTrace();
            // You can throw a custom exception or handle it as needed
            throw new RuntimeException("Failed to create Stripe session: " + e.getMessage());
        }
       
    }

    private String getCurrentUserName() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : "Anonymous";
    }
    
    public void cancelSubscription(String subscriptionId) {
        // Set your Stripe API key
    	Stripe.apiKey = stripeApiKey;
    	
//    	try {
//            // Step 1: List customers with the specified email
//            CustomerListParams params = CustomerListParams.builder()
//                    .setEmail(getCurrentUserName()) // Replace with the customer's email
//                    .build();
//
//            List<Customer> customers = Customer.list(params).getData();
//
//            // Check if customers are found
//            if (customers.isEmpty()) {
//                System.err.println("No customer found with that email.");
//                return; // Exit if no customers found
//            }

            // Step 2: Assuming we take the first customer (you may want to handle multiple customers)
//            Customer customer = customers.get(0);
//            String customerId = customer.getId();
//            String subscriptionId = users.getSubscriptionId();

            // Step 3: Retrieve the subscription using the provided subscriptionId
    	try {
            // Step 3: Retrieve the subscription using the provided subscriptionId
            Subscription subscription = Subscription.retrieve(subscriptionId);

            // Step 4: Update the subscription to cancel it at the end of the period
            SubscriptionUpdateParams cancelParams = SubscriptionUpdateParams.builder()
                    .setCancelAtPeriodEnd(true)
                    .build();

            subscription.update(cancelParams);

            System.out.println("Subscription canceled successfully.");
            
        } catch (StripeException e) {
            // Handle errors that may occur during the cancellation process
            System.err.println("Error canceling subscription: " + e.getMessage());
        }
    }
}
    
    
//    public void processSessionCompleted(Event event) {
//        Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
//         
//        optionalStripeObject.ifPresentOrElse(stripeObject -> {
//            // Cast the StripeObject to a Session object
//            Session session = (Session) stripeObject;
//            
//            // Generate SessionRetrieveParams to expand "payment_intent"
//            SessionRetrieveParams sessionRetrieveParams = SessionRetrieveParams.builder()
//                .addExpand("payment_intent")
//                .build();
//
//            try {
//                Session retrievedSession = Session.retrieve(session.getId(), sessionRetrieveParams, null);
//                Map<String, String> metadata = retrievedSession.getPaymentIntentObject().getMetadata();
//                userService.createCheckoutSession(metadata);
//                System.out.println("Reservation information registration succeeded");
//            } catch (StripeException e) {
//                System.out.println("Reservation information registration failed");
//                throw new RuntimeException("Failed to register reservation information", e);  // Optionally re-throw
//            }
//
//        }, () -> {
//            System.out.println("Failed to deserialize session object from event 2.");
//        });
//
//        // Print the Stripe API version and stripe-java version for logging
//        System.out.println("Stripe API Version: " + event.getApiVersion());
//        System.out.println("stripe-java Version: " + Stripe.VERSION + ", stripe-java API Version: " + Stripe.API_VERSION);
//    }
