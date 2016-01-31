package member;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import library.*;

public class MemberImpl implements Member {

	private MemberData memberData;
	private LibraryServerImpl libraryServerImpl;

	/**
	 * Default constructor of the member client. Initializes variables. You may
	 * add other constructors if you need.
	 * 
	 */
	public MemberImpl() {
		// IMPLEMENT THIS
	}

	public MemberImpl(MemberData md, LibraryServerImpl libraryServerImpl) {
		// IMPLEMENT THIS
		this.memberData = md;
		this.libraryServerImpl = libraryServerImpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see member.Member#getName()
	 */
	public String getName() throws RemoteException {
		// IMPLEMENT THIS
		return memberData.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see member.Member#register()
	 */
	public boolean register() throws RemoteException {
		// IMPLEMENT THIS
		int memberId = libraryServerImpl.registerMember(this.memberData);
		if (memberId > 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see member.Member#checkoutBook(java.lang.String)
	 */
	public boolean checkoutBook(String bookName) throws RemoteException {
		// IMPLEMENT THIS
		Book book = libraryServerImpl.checkoutBook(bookName, this.memberData);
		if (book != null) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see member.Member#returnBook(java.lang.String)
	 */
	public boolean returnBook(String bookName) throws RemoteException {
		// IMPLEMENT THIS
		return libraryServerImpl.returnBook(bookName, this.memberData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see member.Member#getServer()
	 */
	public LibraryServer getServer() throws RemoteException {
		// IMPLEMENT THIS
		return libraryServerImpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see member.Member#setServer(library.LibraryServer)
	 */
	public void setServer(LibraryServer server) throws RemoteException {
		// IMPLEMENT THIS
		this.libraryServerImpl = (LibraryServerImpl) server;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see member.Member#getBooksCheckedOut()
	 */
	public List<Book> getBooksCurrCheckedOut() throws RemoteException {
		// IMPLEMENT THIS

		return new ArrayList<Book>(this.memberData.getBooksCurrCheckedOut());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see member.Member#getBooksRead()
	 */
	public List<String> getBooksRead() throws RemoteException {
		// IMPLEMENT THIS
		return new ArrayList<String>(this.memberData.getBooksRead());
	}

	public static void main(String[] args) {

	}

}
