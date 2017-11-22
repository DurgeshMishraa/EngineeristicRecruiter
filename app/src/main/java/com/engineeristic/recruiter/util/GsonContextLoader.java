package com.engineeristic.recruiter.util;

import com.google.gson.Gson;


public class GsonContextLoader {

 private  final static Gson context = new Gson();

 private GsonContextLoader() {
 }

 public static Gson getGsonContext() {
  return context;
 }

}