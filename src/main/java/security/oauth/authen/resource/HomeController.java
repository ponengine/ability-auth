/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security.oauth.authen.resource;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import security.oauth.authen.util.Helper;

/**
 *
 * @author OREO
 */
@RestController
public class HomeController {
    @GetMapping("/test")
    public String msg(){
    return Helper.getMessage("token_error");
    }
    
}
