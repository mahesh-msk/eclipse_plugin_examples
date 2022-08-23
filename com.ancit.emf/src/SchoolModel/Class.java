/**
 */
package SchoolModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link SchoolModel.Class#getStandard <em>Standard</em>}</li>
 *   <li>{@link SchoolModel.Class#getStudents <em>Students</em>}</li>
 * </ul>
 *
 * @see SchoolModel.SchoolModelPackage#getClass_()
 * @model
 * @generated
 */
public interface Class extends EObject {
	/**
	 * Returns the value of the '<em><b>Standard</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Standard</em>' attribute.
	 * @see #setStandard(String)
	 * @see SchoolModel.SchoolModelPackage#getClass_Standard()
	 * @model
	 * @generated
	 */
	String getStandard();

	/**
	 * Sets the value of the '{@link SchoolModel.Class#getStandard <em>Standard</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Standard</em>' attribute.
	 * @see #getStandard()
	 * @generated
	 */
	void setStandard(String value);

	/**
	 * Returns the value of the '<em><b>Students</b></em>' containment reference list.
	 * The list contents are of type {@link SchoolModel.Student}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Students</em>' containment reference list.
	 * @see SchoolModel.SchoolModelPackage#getClass_Students()
	 * @model containment="true"
	 * @generated
	 */
	EList<Student> getStudents();

} // Class
