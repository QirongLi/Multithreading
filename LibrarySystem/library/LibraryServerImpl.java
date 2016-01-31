package library;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import member.MemberData;
import member.MemberImpl;

public class LibraryServerImpl implements LibraryServer {

	private int numBooks;
	private int copiesPerBook;
	private int booksPerMember;
	private ArrayList<MemberData> memberList;
	// private ArrayList<MemberImpl> memberImpls;
	private ArrayList<String> books;
	private HashMap<String, Integer> bookStatus;

	/**
	 * Constructor for the library server. It is given a number total books to
	 * have, number of copies per book, and maximum books per member. Creates a
	 * number of Book objects based on numBooks to give them to members when
	 * checking them out. The server maintains the properties and enforces them
	 * for future transactions.
	 * 
	 * @param numBooks
	 * @param copiesPerBook
	 * @param booksPerMember
	 */
	public LibraryServerImpl(int numBooks, int copiesPerBook, int booksPerMember) {
		// IMPLEMENT THIS
		this.numBooks = numBooks;
		this.copiesPerBook = copiesPerBook;
		this.booksPerMember = booksPerMember;
		memberList = new ArrayList<MemberData>();
		bookStatus = new HashMap<String, Integer>();
		books = new ArrayList<String>();
		for (int i = 0; i < numBooks; i++) {
			books.add("Book" + i + 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see library.LibraryServer#registerMember(member.Member)
	 */
	@Override
	public Integer registerMember(MemberData memberdata) throws RemoteException {
		// IMPLEMENT THIS
		int memberId = memberList.size() + 1;
		memberdata.setMemberId(memberId);
		// MemberImpl memberImpl = new MemberImpl(memberdata);
		memberList.add(memberdata);
		// memberImpls.add(memberImpl);
		return memberId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see library.LibraryServer#checkoutBook(java.lang.String, member.Member)
	 */
	@Override
	public Book checkoutBook(String bookName, MemberData memberdata)
			throws RemoteException {
		// IMPLEMENT THIS
		if (memberdata.getMemberId() > memberList.size() + 1
				|| memberdata.getMemberId() < 1) {
			return null;
		}

		if ((!bookStatus.containsKey(bookName) || (bookStatus.get(bookName) < copiesPerBook))
				&& memberdata.getBooksCurrCheckedOut().size() < booksPerMember
				&& !memberdata.getBooksCurrCheckedOut().contains(bookName)) {
			Book book = new Book(bookName);
			memberdata.getBooksCurrCheckedOut().add(book);
			bookStatus.put(bookName, bookStatus.get(bookName) + 1);
			return book;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see library.LibraryServer#returnBook(java.lang.String, member.Member)
	 */
	@Override
	public boolean returnBook(String bookName, MemberData memberdata)
			throws RemoteException {
		// IMPLEMENT THIS
		if (memberdata.getMemberId() > memberList.size() + 1
				|| memberdata.getMemberId() < 1) {
			return false;
		}

		if (!memberdata.getBooksCurrCheckedOut().contains(bookName)) {
			return false;
		}

		memberdata.getBooksCurrCheckedOut().remove(bookName);
		memberdata.getBooksRead().add(bookName);
		bookStatus.put(bookName, bookStatus.get(bookName) - 1);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see library.LibraryServer#getBookListings()
	 */
	@Override
	public List<String> getBookListings() throws RemoteException {
		// IMPLEMENT THIS
		List<String> booksCopy = new ArrayList<String>(books);
		return booksCopy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see library.LibraryServer#getAvailableBookListings()
	 */
	@Override
	public List<String> getAvailableBookListings() throws RemoteException {
		// IMPLEMENT THIS
		List<String> booksCopy = new ArrayList<String>(books);
		for (String key : bookStatus.keySet()) {
			if (bookStatus.get(key) == copiesPerBook) {
				booksCopy.remove(key);
			}
		}
		return booksCopy;
	}

	public static void main(String[] args) {

	}

}
