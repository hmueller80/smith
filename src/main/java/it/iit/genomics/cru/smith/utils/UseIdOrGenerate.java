package it.iit.genomics.cru.smith.utils;

import it.iit.genomics.cru.smith.entity.Sample;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
//import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentityGenerator;
//import org.hibernate.id.SequenceGenerator;


/**
 *
 * @author yvaskin
 */
public class UseIdOrGenerate extends IdentityGenerator {

@Override
public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {
    if (obj == null) throw new HibernateException(new NullPointerException()) ;
    //ALTER TABLE sample AUTO_INCREMENT = 1;
    if ((((Sample) obj).getId()) == 0) {        
        Serializable id = super.generate(session, obj) ;
        //System.out.println("generating new sample id");
        return id;
    } else {
        //System.out.println("current sample id " + ((Sample) obj).getId());
        return ((Sample) obj).getId();

    }
}
}