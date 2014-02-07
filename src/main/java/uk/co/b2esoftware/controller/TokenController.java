package uk.co.b2esoftware.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.b2esoftware.TokenDetails;
import uk.co.b2esoftware.TokenManagement;
import uk.co.b2esoftware.vo.TokenVO;

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
    private TokenManagement tokenManagement;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getTokens(ModelMap model)
    {
        List<TokenVO> listTokenVO = listTokenVO(tokenManagement.listExternalToken());
        model.addAttribute("tokens", listTokenVO);
        return "token";
    }

    @RequestMapping(value = "/generate/{roles}", method = RequestMethod.GET)
    public String generateToken(ModelMap model, @PathVariable String roles)
    {
        tokenManagement.generateToken(roles);

        List<TokenVO> listTokenVO = listTokenVO(tokenManagement.listExternalToken());
        model.addAttribute("tokens", listTokenVO);

        return "token";
    }

    @RequestMapping(value = "/delete/{token}", method = RequestMethod.GET)
    public String deleteToken(ModelMap model, @PathVariable String token)
    {
        tokenManagement.invalidateToken(token);

        List<TokenVO> listTokenVO = listTokenVO(tokenManagement.listExternalToken());
        model.addAttribute("tokens", listTokenVO);

        return "token";
    }

    private List<TokenVO> listTokenVO(List<TokenDetails> listTokenDetails)
    {
        List<TokenVO> listTokenVO = new ArrayList<TokenVO>();

        for (TokenDetails tokenDetails : listTokenDetails)
        {
            TokenVO tokenVO = new TokenVO(tokenDetails);
            listTokenVO.add(tokenVO);
        }

        return listTokenVO;
    }
}
