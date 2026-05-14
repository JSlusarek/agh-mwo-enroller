package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    public Collection<Participant> getAll(String key) {
        Query query = connector.getSession().createQuery("FROM Participant WHERE login LIKE :key");
        query.setParameter("key", "%" + key + "%");
        return query.list();
    }

    public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
        String order = "ASC";
        if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
            order = "DESC";
        }

        String column = "login";
        if (sortBy == null || !sortBy.equalsIgnoreCase("login")) {
            if (key != null) {
                return getAll(key);
            }
            return getAll();
        }

        if (key != null) {
            Query query = connector.getSession().createQuery(
                    "FROM Participant WHERE login LIKE :key ORDER BY " + column + " " + order);
            query.setParameter("key", "%" + key + "%");
            return query.list();
        }

        String hql = "FROM Participant ORDER BY " + column + " " + order;
        return connector.getSession().createQuery(hql).list();
    }


    public Participant findByLogin(String login) {



        return (Participant) connector.getSession().get(Participant.class, login);

    }

    public void add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().saveOrUpdate(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();

    }

    public void update(Participant participant, String password) {
        Transaction transaction = connector.getSession().beginTransaction();
        participant.setPassword(password);
        connector.getSession().update(participant);
        transaction.commit();
    }
}



