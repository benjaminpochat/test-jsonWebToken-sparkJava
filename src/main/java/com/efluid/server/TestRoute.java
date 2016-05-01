package com.efluid.server;

import java.io.*;
import java.security.SignatureException;

import com.auth0.jwt.*;
import spark.*;

class TestRoute {
  Object handleLogin(Request request, Response response) throws Exception {

    AuthenticationProcess authenticationProcess = new AuthenticationProcess();
    authenticationProcess.authenticate();

    JSONWebTokenGenerator jwtGenerator = new JSONWebTokenGenerator();
    String jwt = jwtGenerator.generateJSONWebToken();
    String htmlResponse = getHtml("D:/java/workspaces/developpement_dev/formation-devoxx-2016/sparkJava-JWT/src/main/resources/com/efluid/server/login.html");
    htmlResponse = htmlResponse.replaceAll("@jwt", jwt);
    response.cookie("jwt", jwt);

    return htmlResponse;
  };

  Object handleHello(Request request, Response response) throws Exception {
    String jwt = request.cookie("jwt");
    
    String statut;
    if(jwt != null && !jwt.isEmpty()){
      JWTVerifier jwtVerifier = new JWTVerifier("TOTO");
      
      statut = "loggé";
      try{
        jwtVerifier.verify(jwt);
      }catch(SignatureException e){
        statut = "jamais loggé";
      }catch(JWTVerifyException e){
        statut = "jeté";
      }
    } else {
      statut = "déloggé";
    }
    String htmlResponse = getHtml("D:/java/workspaces/developpement_dev/formation-devoxx-2016/sparkJava-JWT/src/main/resources/com/efluid/server/hello.html");
    htmlResponse = htmlResponse.replaceAll("@statut", statut);
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

    String htmlResponse = getHtml("D:/java/workspaces/developpement_dev/formation-devoxx-2016/sparkJava-JWT/src/main/resources/com/efluid/server/logout.html");
    response.cookie("jwt", "");

    return htmlResponse;
  };  
}

