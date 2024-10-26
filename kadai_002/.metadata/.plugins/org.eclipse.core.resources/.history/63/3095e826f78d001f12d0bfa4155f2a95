package com.example.nagoyameshi.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.ApiConnectionException;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.PermissionException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	// セッションを作成し、Stripeに必要な情報を返す
	
	@Value("${stripe.api-key}")
    private String stripeApiKey;
	
    @Value("${stripe.success-url}")
    private String stripeSuccessUrl;

    // 決済キャンセル時のリダイレクト先URL
    @Value("${stripe.cancel-url}")
    private String stripeCancelUrl;
	
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
        		  .setSuccessUrl(stripeSuccessUrl)
        		  .setCancelUrl(stripeCancelUrl)
        		  .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
        		  .addLineItem(new SessionCreateParams.LineItem.Builder()
        		    // For metered billing, do not pass quantity
        		    .setQuantity(1L)
        		    .setPrice(priceId)
        		    .build()
        		  )
        		  .setCustomerEmail(getCurrentUserName()) 
//        		  .setPaymentIntentData(
//	                  SessionCreateParams.PaymentIntentData.builder()
//	                  	  .putMetadata("userName", getCurrentUserName())
//	                      .putMetadata("signupDate", LocalDate.now().toString())
//	                      .build())
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
    
    public void processSessionCompleted(Event event) {
    	 Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
    	 
         optionalStripeObject.ifPresentOrElse(stripeObject -> {
             // StripeObjectオブジェクトをSessionオブジェクトに型変換する
             Session session = (Session)stripeObject;
             
             // "payment_intent"情報を展開する（詳細情報を含める）ように指定したSessionRetrieveParamsオブジェクトを生成する
             SessionRetrieveParams sessionRetrieveParams = SessionRetrieveParams.builder().addExpand("payment_intent").build();
            
             try {
				// 支払い情報を含む詳細なセッション情報を取得する
                session = Session.retrieve(session.getId(),  sessionRetrieveParams, null);
                
                // 詳細なセッション情報からメタデータ（予約情報）を取り出す
                Map<String, String> sessionMetadata = session.getPaymentIntentObject().getMetadata();

                // 予約情報をデータベースに登録する
                userService.createCheckoutSession(sessionMetadata);

                System.out.println("予約情報の登録処理が成功しました。");

            } catch (RateLimitException e) {
                System.out.println("短時間のうちに過剰な回数のAPIコールが行われました。");
            } catch (InvalidRequestException e) {
                System.out.println("APIコールのパラメーターが誤っているか、状態が誤っているか、方法が無効でした。");
            } catch (PermissionException e) {
                System.out.println("このリクエストに使用されたAPIキーには必要な権限がありません。");
            } catch (AuthenticationException e) {
                System.out.println("Stripeは、提供された情報では認証できません。");
            } catch (ApiConnectionException e) {
                System.out.println("お客様のサーバーとStripeの間でネットワークの問題が発生しました。");
            } catch (ApiException e) {
                System.out.println("Stripe側で問題が発生しました（稀な状況です）。");
            } catch (StripeException e) {
                System.out.println("Stripeとの通信中に予期せぬエラーが発生しました。");
            } catch (Exception e) {
                System.out.println("予約情報の登録処理中に予期せぬエラーが発生しました。");
            }
        },
        () -> {
            System.out.println("予約情報の登録処理が失敗しました。");
        });

        // StripeのAPIとstripe-javaライブラリのバージョンをコンソールに出力する
        System.out.println("Stripe API Version: " + event.getApiVersion());
        System.out.println("stripe-java Version: " + Stripe.VERSION + ", stripe-java API Version: " + Stripe.API_VERSION);
    }
}