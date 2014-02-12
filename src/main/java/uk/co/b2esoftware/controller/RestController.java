package uk.co.b2esoftware.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Manuel DEQUEKER on 03/02/2014.
 */
@Controller
@RequestMapping("/rest")
public class RestController
{
    @RequestMapping(value = "/userservice", method = RequestMethod.GET)
    public ResponseEntity<String> userService()
    {
        String typeService = "userservice - OK";

        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(typeService, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/adminservice", method = RequestMethod.GET)
    public ResponseEntity<String> adminService()
    {
        String typeService = "adminservice - OK";

        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(typeService, responseHeaders, HttpStatus.OK);
    }
}
