package com.ge.predix.alm.services;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AssetDataManagerController {

  private static final Logger log = Logger.getLogger(AssetDataManagerController.class);

  @RequestMapping(value = "/", method = RequestMethod.GET)
  @ResponseBody
  @Cacheable("listAssets")
  public String viewAssets() {
    log.info("Performing Expensive Lookup and joins etc......");
    // Performing Expensive Lookup and joins etc
    return "Your Assets List are WIP and coming SOON.....";
  }
}