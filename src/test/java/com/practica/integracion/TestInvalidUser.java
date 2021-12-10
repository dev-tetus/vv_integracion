package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SystemManager test cases for invalid user")
public class TestInvalidUser {
	@Mock
	private AuthDAO authDAO;
	@Mock
	private GenericDAO genericDAO;

	private final User validUser = new User("1", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
	private  final String remoteId = "1452";
	private final String invalidRemoteId = "2541";
	private  final ArrayList<Object> remote = new ArrayList<>(Arrays.asList("uno","dos"));
	private SystemManager sm;
	private InOrder ordered ;

	@DisplayName("Should not start remote")
	@Test
	public void shouldNotStartRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException {

		when(authDAO.getAuthData(validUser.getId())).thenReturn(null);
		when(genericDAO.getSomeData(null, "where id=" + remoteId)).thenThrow(OperationNotSupportedException.class);
		ordered = inOrder(authDAO, genericDAO);
		sm = new SystemManager(authDAO,genericDAO);

		assertThrows(SystemManagerException.class, () -> { Collection<Object> collection = sm.startRemoteSystem(validUser.getId(), remoteId);});
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).getSomeData(null, "where id=" + remoteId);
	}

	@DisplayName("Should not stop remote")
	@Test
	public void shouldNotStopRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException {

		when(authDAO.getAuthData(validUser.getId())).thenReturn(null);
		when(genericDAO.getSomeData(null, "where id=" + remoteId)).thenThrow(OperationNotSupportedException.class);
		InOrder ordered = inOrder(authDAO, genericDAO);
		SystemManager sm = new SystemManager(authDAO, genericDAO);

		assertThrows(SystemManagerException.class, () ->{ Collection<Object> remoteCollection = sm.stopRemoteSystem(validUser.getId(), remoteId);});
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).getSomeData(null, "where id=" + remoteId);
	}

	@DisplayName("Should not add remote")
	@Test
	public void shouldNotAddRemoteSystemTest() throws OperationNotSupportedException {
		when(authDAO.getAuthData(validUser.getId())).thenReturn(null);
		when(genericDAO.updateSomeData(null, remote)).thenThrow(OperationNotSupportedException.class);
		ordered = inOrder(authDAO, genericDAO);
		sm = new SystemManager(authDAO, genericDAO);

		assertThrows(SystemManagerException.class, () ->{
			sm.addRemoteSystem(validUser.getId(), remote);
		});
		ordered.verify(authDAO).getAuthData(validUser.getId());
		ordered.verify(genericDAO).updateSomeData(null, remote);
	}

	@DisplayName("Should not delete remote")
	@Test
	public void shouldNotDeleteRemoteSystemTest() throws OperationNotSupportedException {
		lenient().when(authDAO.getAuthData(validUser.getId())).thenReturn(null);
		lenient().when(genericDAO.deleteSomeData(null, remoteId)).thenThrow(OperationNotSupportedException.class);
		ordered = inOrder(authDAO, genericDAO);
		sm = new SystemManager(authDAO, genericDAO);

		try {
			sm.deleteRemoteSystem(validUser.getId(), remoteId);
			fail("Expected exception was not thrown");

		} catch (SystemManagerException e) {
			assertNotNull(e);
		}
		finally{
			ordered.verify(authDAO).getAuthData(validUser.getId());
			ordered.verify(genericDAO).deleteSomeData(null, remoteId);
		}

	}

	/**
	 * RELLENAR POR EL ALUMNO
	 */
}
