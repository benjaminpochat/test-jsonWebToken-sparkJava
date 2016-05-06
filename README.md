# test-jsonWebToken-sparkJava
Un test de mise en place d'une authentification JsonWebToken (JWT) à l'aide de :
- l'implémentation Java de JWT d'auth0 (cf https://github.com/auth0/java-jwt)
- du micro framework SparkJava (http://sparkjava.com)

Le JWT est placé dans un cookie du navigateur.
Il est renouvelé si l'utilisateur est actif "un peu avant" la date d'expiration du token.
