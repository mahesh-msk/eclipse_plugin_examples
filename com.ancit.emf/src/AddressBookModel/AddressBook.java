/**
 */
package AddressBookModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Address Book</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link AddressBookModel.AddressBook#getName <em>Name</em>}</li>
 *   <li>{@link AddressBookModel.AddressBook#getGroup <em>Group</em>}</li>
 * </ul>
 *
 * @see AddressBookModel.AddressBookModelPackage#getAddressBook()
 * @model
 * @generated
 */
public interface AddressBook extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see AddressBookModel.AddressBookModelPackage#getAddressBook_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link AddressBookModel.AddressBook#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Group</b></em>' containment reference list.
	 * The list contents are of type {@link AddressBookModel.Group}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' containment reference list.
	 * @see AddressBookModel.AddressBookModelPackage#getAddressBook_Group()
	 * @model containment="true"
	 * @generated
	 */
	EList<Group> getGroup();

} // AddressBook
