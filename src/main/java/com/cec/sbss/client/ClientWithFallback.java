package com.cec.sbss.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Component
public class ClientWithFallback {


    private static final String NORMAL = "normal";

    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<String> noFallback(){
        return new ResponseEntity<>(NORMAL, HttpStatus.OK);
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<String> exceptionFallback(boolean runClean) throws ClientException{

        if(!runClean){
            throw new ClientException("not normal path");
        } else {
            return new ResponseEntity<>(NORMAL, HttpStatus.OK);
        }
    }

    @HystrixCommand(
            fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
            })
    public ResponseEntity<String> timeoutFallback(){

        try{

            Thread.sleep(1000);

            return new ResponseEntity<>(NORMAL, HttpStatus.OK);

        } catch(InterruptedException ie){
            return null;
        }
    }

    @HystrixCommand
    public ResponseEntity<String> fallback(){
        return new ResponseEntity<>("fallback", HttpStatus.OK);
    }

    public static class ClientException extends Exception{

        private static final long serialVersionUID = 1L;

        public ClientException(String message){
            super(message);
        }

    }

}
