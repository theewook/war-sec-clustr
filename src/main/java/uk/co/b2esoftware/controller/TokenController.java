package uk.co.b2esoftware.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.b2esoftware.entity.Token;
import uk.co.b2esoftware.service.TokenService;
import uk.co.b2esoftware.vo.TokenVO;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuel DEQUEKER on 03/02/2014.
 */
@Controller
@RequestMapping("/token")
public class TokenController
{
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getTokens(ModelMap model, Principal principal)
    {
        List<TokenVO> listTokenVO = new ArrayList<TokenVO>();

        for (Token tokenDetails : tokenService.listExternalToken())
        {
            TokenVO tokenVO = new TokenVO(tokenDetails);
            listTokenVO.add(tokenVO);
        }

        model.addAttribute("tokens", listTokenVO);

        String name = principal.getName();
        model.addAttribute("username", name);

        return "token";
    }

    @RequestMapping(value = "/generate/{roles}", method = RequestMethod.GET)
    public String generateToken(ModelMap model, Principal principal, @PathVariable String roles)
    {
        tokenService.generateToken(roles);
        return "redirect:/token/list";
    }

    @RequestMapping(value = "/delete/{token}", method = RequestMethod.GET)
    public String deleteToken(ModelMap model, Principal principal, @PathVariable String token)
    {
        tokenService.invalidateToken(token);
        return "redirect:/token/list";
    }
}
