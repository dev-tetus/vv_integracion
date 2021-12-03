package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;

import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestValidUser {
	@Mock
	private AuthDAO authDAO;
	@Mock
	private GenericDAO genericDAO;
	@Test
	public void startRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException {
		User validUser = new User("1", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
		String remoteId = "1452";
		ArrayList<Object> remote = new ArrayList<>(Arrays.asList("uno","dos"));

		when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
		when(genericDAO.getSomeData(validUser, "where id=" + remoteId)).thenReturn(remote);
		InOrder ordered = inOrder(authDAO, genericDAO);
		SystemManager sm = new SystemManager(authDAO,genericDAO);
		Collection<Object> collection = sm.startRemoteSystem(validUser.getId(), remoteId);

		assertEquals(collection.toString(), "[uno, dos]");
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).getSomeData(validUser, "where id=" + remoteId);
	}
	@Test
	public void stopRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException {
		User validUser = new User("1", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
		String remoteId = "1452";
		ArrayList<Object> remote = new ArrayList<>(Arrays.asList("uno","dos"));

		when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
		when(genericDAO.getSomeData(validUser, "where id=" + remoteId)).thenReturn(remote);
		InOrder ordered = inOrder(authDAO, genericDAO);
		SystemManager sm = new SystemManager(authDAO, genericDAO);
		Collection<Object> remoteCollection = sm.stopRemoteSystem(validUser.getId(), remoteId);

		assertEquals(remoteCollection.toString(),"[uno, dos]");
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).getSomeData(validUser, "where id=" + remoteId);
	}
	@Test
	public void addRemoteSystemTest() throws OperationNotSupportedException {
		User validUser = new User("1", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
		ArrayList<Object> remote = new ArrayList<>(Arrays.asList("uno","dos"));

		when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
		when(genericDAO.updateSomeData(validUser, remote)).thenReturn(true);

		InOrder ordered = inOrder(authDAO, genericDAO);
		SystemManager sm = new SystemManager(authDAO, genericDAO);
		assertDoesNotThrow(() ->{
			sm.addRemoteSystem(validUser.getId(), remote);
		});
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).updateSomeData(validUser, remote);
	}

	/*
	@Test
	public void deleteRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException {
		User validUser = new User("1", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
		String remoteId = "1452";
		ArrayList<Object> remote = new ArrayList<>(Arrays.asList("uno","dos"));

		Mockito.lenient().when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
		Mockito.lenient().when(genericDAO.deleteSomeData(validUser, remoteId)).thenReturn(true);

		InOrder ordered = inOrder(authDAO, genericDAO);
		SystemManager sm = new SystemManager(authDAO, genericDAO);
		assertDoesNotThrow(() ->{
			sm.deleteRemoteSystem(validUser.getId(), remoteId);
		});
	}
	*/
	/**
	 * RELLENAR POR EL ALUMNO
	 */

}
