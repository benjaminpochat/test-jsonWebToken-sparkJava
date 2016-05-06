package com.efluid.server;

import java.util.*;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTSigner.Options;

class JSONWebTokenGenerator {
  Map<String, Object> getClaims(){
    Map<String, Object> claims = new HashMap<>();
    claims.put("name", "Asterix");
    claims.put("potion", Boolean.TRUE);
    // La date de délivrance du token permet de renouveler le token si on est actif "peu" avant l'expiration 
    claims.put("dateDelivranceJWT", (new Date()).getTime());
    return claims;
  }
  
  String generateJSONWebToken(){
    JWTSigner jwtSigner = new JWTSigner("TOTO");
    Options options = new Options();
    options.setExpirySeconds(60);
    String token = jwtSigner.sign(getClaims(), options);
    return token;
  }
}
