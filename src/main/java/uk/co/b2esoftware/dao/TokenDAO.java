package uk.co.b2esoftware.dao;

import uk.co.b2esoftware.entity.Token;

import java.util.List;

/**
 * Created by TheEwook on 10/02/2014.
 */
public interface TokenDAO extends GenericDao
{
    public List<Token> getExternalToken();

    public List<Token> getInternalToken();
}
