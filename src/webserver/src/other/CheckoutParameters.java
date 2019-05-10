package other;

import java.util.Vector;

public class CheckoutParameters {

	public class Article {
		private long article_id, quantity;
		
		public Article() {
			super();
		}

		public long getArticle_id() {
			return article_id;
		}

		public long getQuantity() {
			return quantity;
		}
	}
	
	public class NewPaymentMethod {
		private String paypal_email;
		private String creditcard_card_number, creditcard_expire, creditcard_first_name, creditcard_last_name;
		
		public NewPaymentMethod() {
			super();
		}
		
		public String getPaypal_email() {
			return paypal_email;
		}
		public String getCreditcard_card_number() {
			return creditcard_card_number;
		}
		public String getCreditcard_expire() {
			return creditcard_expire;
		}
		public String getCreditcard_first_name() {
			return creditcard_first_name;
		}
		public String getCreditcard_last_name() {
			return creditcard_last_name;
		}
		
	}
	
	private Vector<CheckoutParameters.Article> articles;
	private String paypal_email, card_number;
	private NewPaymentMethod payment_new;
	
	public CheckoutParameters() {
		super();
	}
	
	public Vector<CheckoutParameters.Article> getArticles() {
		return articles;
	}

	public String getPaypal_email() {
		return paypal_email;
	}

	public String getCard_number() {
		return card_number;
	}

	public NewPaymentMethod getPayment_new() {
		return payment_new;
	}
	
	
}
