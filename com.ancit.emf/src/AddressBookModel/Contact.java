/**
 */
package AddressBookModel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link AddressBookModel.Contact#getContactName <em>Contact Name</em>}</li>
 *   <li>{@link AddressBookModel.Contact#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @see AddressBookModel.AddressBookModelPackage#getContact()
 * @model
 * @generated
 */
public interface Contact extends EObject {
	/**
	 * Returns the value of the '<em><b>Contact Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contact Name</em>' attribute.
	 * @see #setContactName(String)
	 * @see AddressBookModel.AddressBookModelPackage#getContact_ContactName()
	 * @model
	 * @generated
	 */
	String getContactName();

	/**
	 * Sets the value of the '{@link AddressBookModel.Contact#getContactName <em>Contact Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contact Name</em>' attribute.
	 * @see #getContactName()
	 * @generated
	 */
	void setContactName(String value);

	/**
	 * Returns the value of the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' attribute.
	 * @see #setLocation(String)
	 * @see AddressBookModel.AddressBookModelPackage#getContact_Location()
	 * @model
	 * @generated
	 */
	String getLocation();

	/**
	 * Sets the value of the '{@link AddressBookModel.Contact#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' attribute.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(String value);

} // Contact
