/**
 */
package SchoolModel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Student</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link SchoolModel.Student#getSeatNo <em>Seat No</em>}</li>
 *   <li>{@link SchoolModel.Student#getName <em>Name</em>}</li>
 *   <li>{@link SchoolModel.Student#getDateOfBirth <em>Date Of Birth</em>}</li>
 * </ul>
 *
 * @see SchoolModel.SchoolModelPackage#getStudent()
 * @model
 * @generated
 */
public interface Student extends EObject {
	/**
	 * Returns the value of the '<em><b>Seat No</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Seat No</em>' attribute.
	 * @see #setSeatNo(int)
	 * @see SchoolModel.SchoolModelPackage#getStudent_SeatNo()
	 * @model
	 * @generated
	 */
	int getSeatNo();

	/**
	 * Sets the value of the '{@link SchoolModel.Student#getSeatNo <em>Seat No</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Seat No</em>' attribute.
	 * @see #getSeatNo()
	 * @generated
	 */
	void setSeatNo(int value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see SchoolModel.SchoolModelPackage#getStudent_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link SchoolModel.Student#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Date Of Birth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date Of Birth</em>' attribute.
	 * @see #setDateOfBirth(String)
	 * @see SchoolModel.SchoolModelPackage#getStudent_DateOfBirth()
	 * @model
	 * @generated
	 */
	String getDateOfBirth();

	/**
	 * Sets the value of the '{@link SchoolModel.Student#getDateOfBirth <em>Date Of Birth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date Of Birth</em>' attribute.
	 * @see #getDateOfBirth()
	 * @generated
	 */
	void setDateOfBirth(String value);

} // Student
