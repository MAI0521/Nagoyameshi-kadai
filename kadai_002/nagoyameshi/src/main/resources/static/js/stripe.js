document.addEventListener('DOMContentLoaded', function() {
    const stripe = Stripe('pk_test_51Q9M36G1Q8EB8XUa1JzgsvmXZCtUyDFLeJozJOM78A4gjNbfUNZ3ITbwn0gQOjvCoQYnDxYzPdI2oSpjtQqZsnlp00N7ic3SNK');
    const csrfToken = document.querySelector('input[name="_csrf"]').value;

    const subscribeButton = document.querySelector('#subscribeButton');
    const updateCardForm = document.getElementById('update-card-form');
    const cardElementContainer = document.getElementById('card-element');
    const cardErrors = document.getElementById('card-errors');

    // Initialize Stripe Elements
    const elements = stripe.elements();
    const cardElement = elements.create('card');
    cardElement.mount(cardElementContainer);

    if (subscribeButton) {
        subscribeButton.addEventListener('click', async (event) => {
            event.preventDefault();
            
            const response = await fetch('/user/create-checkout-session', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-Token': csrfToken 
                },
                body: JSON.stringify({
                    priceId: 'price_1Q9M3bG1Q8EB8XUacUbkFtmb' 
                })
            });

            if (!response.ok) {
                const error = await response.text();
                console.error('Error:', error);
                alert('Failed to create checkout session. ' + error);
                return;
            }

            const { sessionId } = await response.json();
            const result = await stripe.redirectToCheckout({ sessionId: sessionId });

            if (result.error) {
                console.error("Error:", result.error);
            }
        });
    } else {
        console.error('Subscribe button not found');
    }

    function retrieveCardInfo(subscriptionId) {
        return fetch('/user/retrieve-card-info', {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-Token': csrfToken // Include CSRF token
            },
            body: JSON.stringify({
                subscriptionId: subscriptionId,
            }),
        })
        .then((response) => response.json());
    }

    function displayCardInfo(subscriptionId) {
	    retrieveCardInfo(subscriptionId)
	        .then((data) => {
	            if (data.error) {
	                console.error('Error fetching card info:', data.error);
	                document.getElementById('card-info').textContent = 'カード情報を取得できませんでした';
	                return;
	            }
	
	            if (data.card) {
	                const cardBrand = data.card.brand || 'Unknown Brand'; 
	                const last4 = data.card.last4 || '****'; 
	                const expiryMonth = (data.card.expMonth && data.card.expMonth.toString().padStart(2, '0')) || '--'; 
	                const expiryYear = (data.card.expYear && data.card.expYear.toString().slice(-2)) || '--'; 
	
	                document.getElementById('card-brand').textContent = cardBrand;
	                document.getElementById('card-last4').textContent = `**** **** **** ${last4}`;
	                document.getElementById('card-expiry').textContent = `${expiryMonth}/${expiryYear}`;
	            } else {
	                console.error('Card info not found in response:', data);
	                document.getElementById('card-info').textContent = 'カード情報が見つかりませんでした';
	            }
	        })
	        .catch((error) => {
	            console.error('Error fetching payment method:', error);
	            document.getElementById('card-info').textContent = 'カード情報を取得できませんでした';
	        });
	}

    // Call to display card info, you should replace 'yourSubscriptionId' with the actual subscription ID
    const subscriptionId = 'yourSubscriptionId'; // Replace with actual subscription ID
    displayCardInfo(subscriptionId);

    updateCardForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const { paymentMethod, error } = await stripe.createPaymentMethod({
            type: 'card',
            card: cardElement,
        });

        if (error) {
            cardErrors.textContent = error.message;
        } else {
            const response = await fetch('/user/update-card', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    paymentMethodId: paymentMethod.id,
                }),
            });

            if (!response.ok) {
                const errorMsg = await response.text();
                console.error('Error updating card:', errorMsg);
                cardErrors.textContent = 'Failed to update card. ' + errorMsg;
                return;
            }

            alert('登録のカードを変更しました');
            displayCardInfo(subscriptionId); // Refresh card info after update
        }
    });
});