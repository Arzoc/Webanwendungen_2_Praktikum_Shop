package other;

import model.Paypal;

import java.util.Vector;

import model.Creditcard;

public class PaymentMethodsCombined {

	private Vector<Paypal> paypals;
	private Vector<Creditcard> creditcards;
	
	/* abstraction for the two tables paypal and creditcard */
	public PaymentMethodsCombined(Vector<Paypal> paypals, Vector<Creditcard> creditcards) {
		super();
		this.paypals = paypals;
		this.creditcards = creditcards;
	}

	public Vector<Paypal> getPaypals() {
		return paypals;
	}

	public Vector<Creditcard> getCreditcards() {
		return creditcards;
	}
	
}
