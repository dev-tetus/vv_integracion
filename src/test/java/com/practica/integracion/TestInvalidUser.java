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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestInvalidUser {
	@Mock
	private AuthDAO authDAO;
	@Mock
	private GenericDAO genericDAO;
	@Test
	public void startRemoteSystemInvalidUserTest() throws OperationNotSupportedException, SystemManagerException {
		User validUser = new User("1", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
		String remoteId = "1452";

		when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
		when(genericDAO.getSomeData(validUser, "where id=" + remoteId)).thenThrow(OperationNotSupportedException.class);
		InOrder ordered = inOrder(authDAO, genericDAO);
		SystemManager sm = new SystemManager(authDAO,genericDAO);

		assertThrows(SystemManagerException.class, () -> { Collection<Object> collection = sm.startRemoteSystem(validUser.getId(), remoteId);});
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).getSomeData(validUser, "where id=" + remoteId);
	}
	@Test
	public void stopRemoteSystemInvalidUserTest() throws OperationNotSupportedException, SystemManagerException {
		User validUser = new User("1", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
		String remoteId = "1452";

		when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
		when(genericDAO.getSomeData(validUser, "where id=" + remoteId)).thenThrow(OperationNotSupportedException.class);
		InOrder ordered = inOrder(authDAO, genericDAO);
		SystemManager sm = new SystemManager(authDAO, genericDAO);

		assertThrows(SystemManagerException.class, () ->{ Collection<Object> remoteCollection = sm.stopRemoteSystem(validUser.getId(), remoteId);});
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).getSomeData(validUser, "where id=" + remoteId);
	}
	@Test
	public void addRemoteSystemInvalidUserTest() throws OperationNotSupportedException {
		User validUser = new User("1", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
		ArrayList<Object> remote = new ArrayList<>(Arrays.asList("uno","dos"));

		Mockito.lenient().when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
		Mockito.lenient().when(genericDAO.updateSomeData(validUser, remote)).thenReturn(true);

		InOrder ordered = inOrder(authDAO, genericDAO);
		SystemManager sm = new SystemManager(authDAO, genericDAO);
		assertDoesNotThrow(() ->{
			sm.addRemoteSystem(validUser.getId(), remote);
		});
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).updateSomeData(validUser, remote);
	}
	@MockitoSettings(strictness = Strictness.LENIENT)
	@Test
	public void deleteRemoteSystemInvalidUserTest() throws OperationNotSupportedException {
		User validUser = new User("2", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
		String remoteId = "1452";
		ArrayList<Object> remote = new ArrayList<>(Arrays.asList("uno","dos"));

		when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
		when(genericDAO.deleteSomeData(validUser, remoteId)).thenThrow(OperationNotSupportedException.class);
		InOrder ordered = inOrder(authDAO, genericDAO);
		SystemManager sm = new SystemManager(authDAO, genericDAO);

		assertThrows(SystemManagerException.class, () ->{ sm.deleteRemoteSystem(validUser.getId(), remoteId);});
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).deleteSomeData(validUser, remoteId);
	}
	/**
	 * RELLENAR POR EL ALUMNO
	 */
}
