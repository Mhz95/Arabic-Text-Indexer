package assignment.obj;

public class Token {
	
		private int tokenID;
		private String token;
		private String token_stem;
		private char type;
			
		public Token(int id, String value, char type) {
			this.tokenID = id;
			this.token = value;
			this.token_stem = "";
			this.type = type;
		}
		
		public int getTokenID() {
			return tokenID;
		}

		public void setToken(int tokenID) {
			this.tokenID = tokenID;
		}

		public String getToken() {
			return token;
		}



		public void setToken(String token) {
			this.token = token;
		}



		public String getToken_stem() {
			return token_stem;
		}



		public void setToken_stem(String token_stem) {
			this.token_stem = token_stem;
		}



		public char getType() {
			return type;
		}



		public void setType(char type) {
			this.type = type;
		}



		@Override
		public String toString() {
			return "Token [ID= "+ tokenID +" | token= " + token + " | stem= " + token_stem + " | type=" + type + "]";
		}



}
