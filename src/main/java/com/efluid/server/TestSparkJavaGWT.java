package com.efluid.server;

import static spark.Spark.get;

import spark.*;

class TestSparkJavaGWT {
  
  public static void main(String[] args) {
    TestRoute route = new TestRoute();
    get("/hello", route::handleHello);
    get("/login", route::handleLogin);
    get("/logout", route::handleLogout);
  }
}
