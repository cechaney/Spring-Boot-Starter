package com.cec.sbss.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cec.sbss.helper.ControllerHelper;

@Controller
@RequestMapping("home")
public class HomeEndpoint {


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showHomeView(
            HttpServletRequest req,
            @ModelAttribute(ControllerHelper.DEBUG) Boolean debug){

        ModelAndView mav = new ModelAndView();

        mav.addObject(ControllerHelper.DEBUG, debug);

        mav.setViewName("home");

        return mav;

    }

}
