package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;

import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.*;
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
@DisplayName("SystemManager test cases for valid user")
public class TestValidUser {
	@Mock
	private AuthDAO authDAO;
	@Mock
	private GenericDAO genericDAO;

	private final User validUser = new User("1", "Alejandro", "Magno", "Pella", new ArrayList<Object>(Arrays.asList(1,2)));
	private  final String remoteId = "1452";
	private final String invalidRemoteId = "2541";
	private  final ArrayList<Object> remote = new ArrayList<>(Arrays.asList("uno","dos"));
	private final ArrayList<Object> emptyRemote = new ArrayList<>(Arrays.asList(" "));
	private SystemManager sm;
	private InOrder ordered ;

	@DisplayName("Start remote test cases")
	@Nested
	public class StartRemoteSystemTestClass{
		@DisplayName("Should start remote")
		@Test
		public void shouldStartRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException {
			when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
			when(genericDAO.getSomeData(validUser, "where id=" + remoteId)).thenReturn(remote);
			sm = new SystemManager(authDAO,genericDAO);
			ordered = inOrder(authDAO, genericDAO);

			Collection<Object> collection = sm.startRemoteSystem(validUser.getId(), remoteId);

			assertEquals(collection.toString(), "[uno, dos]");
			ordered.verify(authDAO).getAuthData(validUser.getId());
			ordered.verify(genericDAO).getSomeData(validUser, "where id=" + remoteId);
		}
		@DisplayName("Should not start remote")
		@Test
		public void shouldNotStartRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException {
			when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
			when(genericDAO.getSomeData(validUser, "where id=" + remoteId)).thenReturn(emptyRemote);
			sm = new SystemManager(authDAO,genericDAO);
			ordered = inOrder(authDAO, genericDAO);

			Collection<Object> collection = sm.startRemoteSystem(validUser.getId(), remoteId);

			assertEquals(collection.toString(), "[ ]");
			ordered.verify(authDAO).getAuthData(validUser.getId());
			ordered.verify(genericDAO).getSomeData(validUser, "where id=" + remoteId);
		}
	}

	@DisplayName("Stop remote system test cases")
	@Nested
	public class StopRemoteSystemTestClass{
		@DisplayName("Should stop remote")
		@Test
		public void shouldStopRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException {
			when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
			when(genericDAO.getSomeData(validUser, "where id=" + remoteId)).thenReturn(remote);
			sm = new SystemManager(authDAO, genericDAO);
			ordered = inOrder(authDAO, genericDAO);

			Collection<Object> remoteCollection = sm.stopRemoteSystem(validUser.getId(), remoteId);

			assertEquals(remoteCollection.toString(),"[uno, dos]");
			ordered.verify(authDAO).getAuthData(validUser.getId());
			ordered.verify(genericDAO).getSomeData(validUser, "where id=" + remoteId);
		}
		@DisplayName("Should not stop remote")
		@Test
		public void shouldNotStopRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException {
			when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
			when(genericDAO.getSomeData(validUser, "where id=" + remoteId)).thenReturn(emptyRemote);
			sm = new SystemManager(authDAO, genericDAO);
			ordered = inOrder(authDAO, genericDAO);

			Collection<Object> remoteCollection = sm.stopRemoteSystem(validUser.getId(), remoteId);

			assertEquals(remoteCollection.toString(),"[ ]");
			ordered.verify(authDAO).getAuthData(validUser.getId());
			ordered.verify(genericDAO).getSomeData(validUser, "where id=" + remoteId);
		}
	}

	@DisplayName("Add remote system test cases")
	@Nested
	public class AddRemoteSystemTestClass{
		@DisplayName("Should add remote")
		@Test
		public void shouldAddRemoteSystemTest() throws OperationNotSupportedException {
			when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
			when(genericDAO.updateSomeData(validUser, remote)).thenReturn(true);
			sm = new SystemManager(authDAO, genericDAO);
			ordered = inOrder(authDAO, genericDAO);

			assertDoesNotThrow(() ->{
				sm.addRemoteSystem(validUser.getId(), remote);
			});
			ordered.verify(authDAO).getAuthData(validUser.getId());
			ordered.verify(genericDAO).updateSomeData(validUser, remote);
		}
		@DisplayName("Should not add remote")
		@Test
		public void shouldNotAddRemoteSystemTest() throws OperationNotSupportedException {
			when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
			when(genericDAO.updateSomeData(validUser, remote)).thenReturn(false);
			sm = new SystemManager(authDAO, genericDAO);
			ordered = inOrder(authDAO, genericDAO);

			assertThrows(SystemManagerException.class, () ->{
				sm.addRemoteSystem(validUser.getId(), remote);
			});
			ordered.verify(authDAO).getAuthData(validUser.getId());
			ordered.verify(genericDAO).updateSomeData(validUser, remote);
		}
	}

	@DisplayName("Delete remote system test cases")
	@Nested
	public class DeleteRemoteSystemTestClass{
		@DisplayName("Should delete remote")
		@Test
		public void shouldDeleteRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException{
			lenient().when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
			lenient().when(genericDAO.deleteSomeData(validUser, remoteId)).thenReturn(true);

			sm = new SystemManager(authDAO, genericDAO);
			ordered = inOrder(authDAO, genericDAO);
			try {
				sm.deleteRemoteSystem(validUser.getId(), remoteId);
			} catch (SystemManagerException e) {
				fail("Unexpected exception was thrown");
			}
			finally{
				ordered.verify(authDAO).getAuthData(validUser.getId());
				ordered.verify(genericDAO).deleteSomeData(validUser, remoteId);
			}
		}
		@DisplayName("Should not delete remote")
		@Test
		public void shouldNotDeleteRemoteSystemTest() throws OperationNotSupportedException, SystemManagerException{
			lenient().when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
			lenient().when(genericDAO.deleteSomeData(validUser, remoteId)).thenReturn(false);

			sm = new SystemManager(authDAO, genericDAO);
			ordered = inOrder(authDAO, genericDAO);
			try {
				sm.deleteRemoteSystem(validUser.getId(), remoteId);
				fail("Expected exception was not thrown");

			} catch (SystemManagerException e) {
				assertNotNull(e);
			}
			finally{
				ordered.verify(authDAO).getAuthData(validUser.getId());
				ordered.verify(genericDAO).deleteSomeData(validUser, remoteId);
			}
		}
	}

	/**
	 * RELLENAR POR EL ALUMNO
	 */

}
