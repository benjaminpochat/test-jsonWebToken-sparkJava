package com.nymajneb.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SignatureException;
import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import spark.Request;
import spark.Response;

class TestRoute {
  Object handleLogin(Request request, Response response) throws Exception {

    AuthenticationProcess authenticationProcess = new AuthenticationProcess();
    authenticationProcess.authenticate();

    JSONWebTokenGenerator jwtGenerator = new JSONWebTokenGenerator();
    String jwt = jwtGenerator.generateJSONWebToken();
    String htmlResponse = getHtml("./src/main/resources/com/efluid/server/login.html");
    htmlResponse = htmlResponse.replaceAll("@jwt", jwt);
    response.cookie("jwt", jwt);

    return htmlResponse;
  };

  Object handleHello(Request request, Response response) throws Exception {
    String jwt = request.cookie("jwt");
    
    String statut;
    boolean refreshToken = false;
    if(jwt != null && !jwt.isEmpty()){
      JWTVerifier jwtVerifier = new JWTVerifier("TOTO");
      
      statut = "loggé";
      
      try{
        Map<String, Object> map = jwtVerifier.verify(jwt);
        Date dateDelivranceJWT = new Date((Long)map.get("dateDelivranceJWT"));
        Integer dateExpirationEnSecondes = (Integer)map.get("exp");
        Date dateExpiration = new Date(dateExpirationEnSecondes * 1000l);
        statut = "loggé depuis <b>" + dateDelivranceJWT + "</b> et jusqu'à <b>" + dateExpiration.toString() + "</b>";

        // si on est actif dans les 30s qui précèdent l'expiration, le token est renouvelé
        Date dateRefresh = new Date(dateExpirationEnSecondes * 1000l - 30000l);
        if(new Date().after(dateRefresh)){
        	refreshToken = true;
        }
      }catch(SignatureException e){
        statut = "jamais loggé";
      }catch(JWTVerifyException e){
        statut = "pas loggé";
      }
    } else {
      statut = "déloggé";
    }
    String htmlResponse = getHtml("./src/main/resources/com/efluid/server/hello.html");
    htmlResponse = htmlResponse.replaceAll("@statut", statut);
    
    if(refreshToken){
    	// renouvellement du token
    	JSONWebTokenGenerator jwtGenerator = new JSONWebTokenGenerator();
    	String jwtRefresh = jwtGenerator.generateJSONWebToken();
    	response.cookie("jwt", jwtRefresh);
    }
    return htmlResponse;
  }
  
  String getHtml(String pagePath) throws IOException{
    BufferedReader reader = new BufferedReader( new FileReader (pagePath));
    String line = null;
    StringBuilder stringBuilder = new StringBuilder();
    String ls = System.getProperty("line.separator");

    try {
      while( ( line = reader.readLine() ) != null ) {
        stringBuilder.append( line );
        stringBuilder.append( ls );
      }
      return stringBuilder.toString();
    } finally {
      reader.close();
    }    
  }
  
  Object handleLogout(Request request, Response response) throws Exception {

    String htmlResponse = getHtml("./src/main/resources/com/efluid/server/logout.html");
    response.cookie("jwt", "");

    return htmlResponse;
  };  
}

