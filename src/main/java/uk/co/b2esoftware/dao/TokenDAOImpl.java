package uk.co.b2esoftware.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import uk.co.b2esoftware.entity.Token;

import java.util.List;

/**
 * Created by TheEwook on 10/02/2014.
 */
@Component(value = "tokenDAO")
public class TokenDAOImpl extends GenericDaoImpl implements TokenDAO
{
    public List<Token> getExternalToken()
    {
        Criteria criteria = getCurrentSession().createCriteria(Token.class)
                .add(Restrictions.eq("external", true));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return (List<Token>) criteria.list();
    }

    public List<Token> getInternalToken()
    {
        Criteria criteria = getCurrentSession().createCriteria(Token.class)
                .add(Restrictions.eq("external", false));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return (List<Token>) criteria.list();
    }
}
