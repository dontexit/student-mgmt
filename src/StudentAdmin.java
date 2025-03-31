import java.util.List;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class StudentAdmin extends Application {

	private static Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	private static final int width = 720;

	private static final int height = 728;

	@Override
	public void start(Stage stage) {
		primaryStage = stage;
		showMainMenu();
		primaryStage.setTitle("Student Course Management");
		primaryStage.show();
	}

	private static void showMainMenu() {
		VBox layout = new VBox(10);
		Button addStudentBtn = new Button("Add Student");
		Button addCourseBtn = new Button("Add Course");
		Button enrollStudentBtn = new Button("Entroll Student");
		Button viewStudentBtn = new Button("View Student");
		Button viewCourseBtn = new Button("View Course");

		addStudentBtn.setOnAction(e -> showAddStudentForm());
		addCourseBtn.setOnAction(e -> showAddCourseForm());
		enrollStudentBtn.setOnAction(e -> showEnrollCourseForm());
		viewStudentBtn.setOnAction(e -> showStudent(null));
		viewCourseBtn.setOnAction(e -> showCourses(null));
		//
		layout.getChildren().addAll(addStudentBtn, addCourseBtn, enrollStudentBtn, viewStudentBtn, viewCourseBtn);

		primaryStage.setScene(new Scene(layout, width, height));
	}

	// private static void showStudent() {
	// Label studentID = new Label("Student ID: ");
	// TextField studentIDField = new TextField("");
	// Button searchBtn = new Button("Search");
	//
	// Label formStatus = new Label("");
	//
	// searchBtn.setOnAction(e -> {
	//
	// String studentIDString = studentIDField.getText().trim();
	//
	// int StudentIDInt;
	// if (!studentIDString.isEmpty()) {
	// try {
	// StudentIDInt = Integer.parseInt(studentIDString);
	// } catch (Exception err) {
	// formStatus.setText("Student ID must be a number.");
	// return;
	// }
	//
	// Student student = Student.getStudent(StudentIDInt);
	//
	// }
	//
	// });

	/* } */

	private static void showAddStudentForm() {
		VBox layout = new VBox(10);
		TextField nameField = new TextField();
		Button submitBtn = new Button("Submit");
		Button backBtn = new Button("Back");
		Label studentNameLabel = new Label("Student Name: ");
		Label formStatus = new Label("");
		submitBtn.setOnAction(e -> {
			submitBtn.setDisable(true);
			String name = nameField.getText().trim();
			if (!name.isEmpty()) {
				Student student = Student.createStudent(name);
				if (student == null) {
					formStatus.setText("Error creating student.");
				} else {
					studentNameLabel.setVisible(false);
					nameField.setVisible(false);
					submitBtn.setVisible(false);
					formStatus.setText(String.format("Student with ID `%d` created sucessfully.", student.getId()));
					nameField.setText("");
				}
				submitBtn.setDisable(false);
			}
		});

		backBtn.setOnAction(e -> {
			formStatus.setText("");
			nameField.setVisible(true);
			submitBtn.setVisible(true);

			studentNameLabel.setVisible(true);

			showMainMenu();
		});

		layout.getChildren().addAll(studentNameLabel, nameField, formStatus, submitBtn, backBtn);
		primaryStage.setScene(new Scene(layout, width, height));
	}

	private static void showAddCourseForm() {
		VBox layout = new VBox(10);
		Label courseNameLabel = new Label("Course name : ");
		Label courseCodeLabel = new Label("Course code : ");

		Label maxGradeLabel = new Label("Maximum grade : ");
		TextField nameField = new TextField();
		TextField codeField = new TextField();
		Label capacityLabel = new Label("Capacity");
		TextField capacityField = new TextField();
		TextField maxGradeField = new TextField();
		Button submitBtn = new Button("Submit");
		Button backBtn = new Button("Back");
		Label statusMsg = new Label("");
		submitBtn.setOnAction(e -> {
			submitBtn.setDisable(true);
			String name = nameField.getText().trim();
			String courseCode = codeField.getText().trim();

			String capacityString = capacityField.getText().trim();
			String maxGradeString = maxGradeField.getText().trim();
			int maxGradeInt;
			int capacityInt;
			try {
				maxGradeInt = Integer.parseInt(maxGradeString);
			} catch (Exception err) {
				statusMsg.setText("Max grade must be a number.");
				submitBtn.setDisable(false);
				return;
			}

			try {
				capacityInt = Integer.parseInt(capacityString);
			} catch (Exception err) {
				statusMsg.setText("Capacity must be a number.");
				submitBtn.setDisable(false);
				return;
			}

			if (!name.isEmpty() && !courseCode.isEmpty() && maxGradeInt > 0 && maxGradeInt <= 100) {
				boolean course = Course.addCourse(name, courseCode, capacityInt, maxGradeInt);
				if (course) {
					statusMsg.setText("Course created sucessfully.");
					nameField.setText("");
					codeField.setText("");
					nameField.setVisible(false);
					codeField.setVisible(false);
					submitBtn.setVisible(false);
					courseNameLabel.setVisible(false);
					courseCodeLabel.setVisible(false);
					maxGradeField.setVisible(false);
					capacityField.setVisible(false);
					capacityLabel.setVisible(false);
					maxGradeLabel.setVisible(false);
				} else {
					statusMsg.setText("Failed to create course. It might already exist. Or value off limits.");
				}
			} else {
				statusMsg.setText("One of the fields is empty.");
			}

		});

		backBtn.setOnAction(e -> {
			statusMsg.setText("");
			nameField.setVisible(true);
			codeField.setVisible(true);
			submitBtn.setVisible(true);
			courseNameLabel.setVisible(true);
			courseCodeLabel.setVisible(true);
			showMainMenu();
		});
		layout.getChildren().addAll(courseNameLabel, nameField, courseCodeLabel, codeField, capacityLabel, capacityField,
				maxGradeLabel,
				maxGradeField,
				statusMsg, submitBtn, backBtn);
		primaryStage.setScene(new Scene(layout, width, height));

	}

	private static void showEnrollCourseForm(Student student) {
		VBox layout = new VBox(10);
		Label studentIDLabel = new Label("Student ID : ");
		TextField idField = new TextField(String.format("%s", student.getName()));
		idField.setDisable(true);

		Label courseCodeLabel = new Label("Course code : ");
		TextField codeField = new TextField();

		Label courseCodeErr = new Label("");
		Label studentIdErr = new Label("");
		Label formStatus = new Label("");

		Button submitBtn = new Button("Submit");
		Button backBtn = new Button("Back");

		submitBtn.setOnAction(e -> {
			String courseCode = codeField.getText().trim();

			if (!courseCode.isEmpty()) {
				Course course = Course.getCourse(courseCode);

				if (course != null && student != null) {
					Boolean enrolledStudent = course.enrollStudent(student);
					if (enrolledStudent) {
						studentIDLabel.setDisable(true);
						idField.setDisable(true);
						courseCodeLabel.setDisable(true);
						codeField.setDisable(true);
						submitBtn.setDisable(true);
						courseCodeErr.setText("");
						studentIdErr.setText("");
						formStatus.setText("Student enrolled sucessfully.");
					}
				} else {
					if (course == null) {
						courseCodeErr.setText(String.format("Course `%s` doesn't exist.", courseCode));
					} else {
						formStatus.setText("Student couldn't be enrolled");
					}
				}

			}
		});
		backBtn.setOnAction(e -> {
			studentIDLabel.setDisable(false);
			idField.setDisable(false);
			courseCodeLabel.setDisable(false);
			codeField.setDisable(false);
			submitBtn.setDisable(false);
			formStatus.setText("");
			showMainMenu();
		});

		layout.getChildren().addAll(studentIDLabel, idField, studentIdErr, courseCodeLabel, codeField, courseCodeErr,
				formStatus,
				submitBtn, backBtn);

		primaryStage.setScene(new Scene(layout, width, height));
	}

	private static void showEnrollCourseForm() {
		VBox layout = new VBox(10);
		Label studentIDLabel = new Label("Student ID : ");
		TextField idField = new TextField();

		Label courseCodeLabel = new Label("Course code : ");
		TextField codeField = new TextField();

		Label courseCodeErr = new Label("");
		Label studentIdErr = new Label("");
		Label formStatus = new Label("");

		Button submitBtn = new Button("Submit");
		Button backBtn = new Button("Back");

		submitBtn.setOnAction(e -> {
			String studentID = idField.getText().trim();
			String courseCode = codeField.getText().trim();

			int StudentIDInt;
			if (!studentID.isEmpty() && !courseCode.isEmpty()) {
				Course course = Course.getCourse(courseCode);
				try {
					StudentIDInt = Integer.parseInt(studentID);
				} catch (Exception err) {
					formStatus.setText("Student ID must be a number.");
					return;
				}

				Student student = Student.getStudent(StudentIDInt);

				if (course != null && student != null) {
					Boolean enrolledStudent = course.enrollStudent(student);
					if (enrolledStudent) {
						studentIDLabel.setDisable(true);
						idField.setDisable(true);
						courseCodeLabel.setDisable(true);
						codeField.setDisable(true);
						submitBtn.setDisable(true);
						courseCodeErr.setText("");
						studentIdErr.setText("");
						formStatus.setText("Student enrolled sucessfully.");
					} else {
						formStatus.setText("Course full or student already enrolled.");
					}

				} else {
					if (course == null) {
						courseCodeErr.setText(String.format("Course `%s` doesn't exist.", courseCode));
					}
					if (student == null) {
						studentIdErr.setText(String.format("Student with ID `%d` doesn't exist.", StudentIDInt));
					} else {
						formStatus.setText("Student couldn't be enrolled");
					}
				}

			}
		});
		backBtn.setOnAction(e -> {
			studentIDLabel.setDisable(false);
			idField.setDisable(false);
			courseCodeLabel.setDisable(false);
			codeField.setDisable(false);
			submitBtn.setDisable(false);
			formStatus.setText("");
			showMainMenu();
		});

		layout.getChildren().addAll(studentIDLabel, idField, studentIdErr, courseCodeLabel, codeField, courseCodeErr,
				formStatus,
				submitBtn, backBtn);

		primaryStage.setScene(new Scene(layout, width, height));
	}

	private static void displayStudents(Integer page) {

		VBox layout = new VBox(10);

	}

	private static void showStudent(Student student) {
		VBox layout = new VBox(10);
		Label studentIDLabel = new Label("Student ID : ");
		TextField idField = new TextField("");

		Label formStatus = new Label();

		Button searchBtn = new Button("Search");
		Button backBtn = new Button("Back");
		AtomicReference<HBox> studentToRenderRef = new AtomicReference<>(null);

		AtomicReference<String> lastSearchedIDRef = new AtomicReference<>("");

		layout.getChildren().addAll(studentIDLabel, idField, formStatus, searchBtn, backBtn);
		int labelIndex = layout.getChildren().indexOf(formStatus);
		if (student != null) {
			lastSearchedIDRef.set(String.format("%d", student.getId()));
			studentToRenderRef.set(StudentComponent(student));
			idField.setText(String.format("%d", student.getId()));
			layout.getChildren().add(labelIndex + 1, studentToRenderRef.get());
		}
		backBtn.setOnAction(e -> {
			showMainMenu();
		});

		searchBtn.setOnAction(e -> {
			String idString = idField.getText().trim();

			if (idString == lastSearchedIDRef.get()) {
				return;
			} else {
				lastSearchedIDRef.set(idString);
			}
			int studentID;
			try {
				studentID = Integer.parseInt(idString);
			} catch (Exception err) {
				formStatus.setText("Student ID should be a number.");
				return;
			}
			Student studentInside = Student.getStudent(studentID);
			if (studentInside != null) {
				if (studentToRenderRef.get() != null) {
					layout.getChildren().remove(studentToRenderRef.get());
				}
				studentToRenderRef.set(StudentComponent(studentInside));
				layout.getChildren().add(labelIndex + 1, StudentComponent(studentInside));
			} else {
				formStatus.setText(String.format("Student with id `%s` not found.", idString));
			}
		});

		primaryStage.setScene(new Scene(layout, width, height));

	}

	private static HBox StudentComponent(Student student) {
		HBox layout = new HBox(10);

		Label nameLabel = new Label("Name :");
		Label name = new Label(student.getName());
		Label idLabel = new Label("ID: ");
		Label id = new Label(String.format("%d", student.getId()));

		Button viewStudent = new Button("View Student");
		Button editStudent = new Button("Edit Student");
		Button enrollCourse = new Button("Entroll Course");

		editStudent.setOnAction(e -> {
			StudentEditForm(student);
		});

		enrollCourse.setOnAction(e -> {
			showEnrollCourseForm(student);
		});

		viewStudent.setOnAction(e -> {
			StudentDetailComponent(student);
		});

		layout.getChildren().addAll(nameLabel, name, idLabel, id, viewStudent, editStudent, enrollCourse);
		return layout;

		// primaryStage.setScene(new Scene(layout, width, height));

	}

	private static void StudentDetailComponent(Student student) {
		VBox layout = new VBox(10);

		Label nameLabel = new Label("Name :");
		Label name = new Label(student.getName());

		Button assignGrades = new Button("Assign Grades");

		HBox nameLine = new HBox();

		nameLine.getChildren().addAll(nameLabel, name);

		Label idLabel = new Label("ID: ");
		Label id = new Label(String.format("%d", student.getId()));

		HBox idLine = new HBox();

		idLine.getChildren().addAll(idLabel, id);

		HashMap<String, Double> grades = student.getCourses();

		Label gradesLabel = new Label("Grades");

		HBox gradeLayout = new HBox(10);
		Label courseCodeLabel = new Label("Course Code");
		Label gradeLabel = new Label("Grade");

		Button backBtn = new Button("Back");

		assignGrades.setOnAction(e -> {
			AssignGradesForm(student);

		});

		gradeLayout.getChildren().addAll(courseCodeLabel, gradeLabel);

		layout.getChildren().addAll(nameLine, idLine, assignGrades, gradesLabel, gradeLayout, backBtn);

		backBtn.setOnAction(e -> {
			showStudent(student);
		}); // VBox = new

		for (Map.Entry<String, Double> entry : grades.entrySet()) {

			Label courseCode = new Label(entry.getKey());

			Double grade = entry.getValue();
			String gradeString = "";
			if (grade == -0.0) {
				gradeString = "N/A";
			} else {
				gradeString = String.format("%f", grade);
			}

			HBox gradeLine = new HBox(3);

			Label courseGrade = new Label(gradeString);
			gradeLine.getChildren().addAll(courseCode, courseGrade);

			int gradeLayoutIndex = layout.getChildren().indexOf(gradeLayout);

			layout.getChildren().add(gradeLayoutIndex + 1, gradeLine);

		}

		primaryStage.setScene(new Scene(layout, width, height));

	}

	private static void AssignGradesForm(Student student) {
		VBox layout = new VBox(10);

		Label coursesLabel = new Label("Courses");

		HBox courseRow = new HBox(5);

		Label courseLabel = new Label("Course");
		Label gradeLabel = new Label("Grade");

		courseRow.getChildren().addAll(courseLabel, gradeLabel);

		Button backBtn = new Button("Back");

		backBtn.setOnAction(e -> {
			showStudent(student);
		});

		layout.getChildren().addAll(coursesLabel, courseRow, backBtn);

		if (student.getCourses().size() < 0) {
			Label notEntrolledLabel = new Label("Student not enrolled in any courses.");
		} else {
			for (Map.Entry<String, Double> entry : student.getCourses().entrySet()) {
				Course course = Course.getCourse(entry.getKey());
				int gradeIndex = layout.getChildren().indexOf(courseRow);
				HBox courseLine = gradeForm(student, course);
				layout.getChildren().add(gradeIndex + 1, courseLine);
			}
		}

		primaryStage.setScene(new Scene(layout, width, height));

	}

	private static HBox gradeForm(Student student, Course course) {

		VBox courseLayout = new VBox(5);

		Double grade = course.getGrade(student);
		Integer maxGrade = course.getMaxGrade();
		HBox courseLine = new HBox(3);
		String courseCode = course.getCode();

		Label courseLabel = new Label(courseCode);
		Label gradeValue = new Label(String.format("%f", grade == -0.00 ? 0.00 : grade));
		Button assignGradeBtn = new Button(grade == -0.0 ? "Assign Grade" : "Change Grade");

		Button backBtn = new Button("Back");
		Button submitBtn = new Button("Submit");
		submitBtn.setVisible(false);

		Button cancelBtn = new Button("Cancel");

		TextField gradeField = new TextField(grade == -0.00 ? "0" : String.format("%f", grade));

		cancelBtn.setOnAction(e -> {
			submitBtn.setVisible(false);
			cancelBtn.setVisible(false);
			assignGradeBtn.setVisible(true);
			gradeField.setVisible(false);
		});

		VBox errorContainer = new VBox(7);

		Label formStatus = new Label("");
		gradeField.setVisible(false);
		assignGradeBtn.setOnAction(e -> {
			assignGradeBtn.setVisible(false);
			gradeField.setVisible(true);
			submitBtn.setVisible(true);
			cancelBtn.setVisible(true);
		});

		submitBtn.setOnAction(e -> {
			submitBtn.setDisable(true);
			String gradeFieldString = gradeField.getText().trim();

			if (gradeFieldString.isEmpty()) {
				formStatus.setText("Grade can't be empty.");
				return;
			}

			Double gradeFieldValue;
			try {
				gradeFieldValue = Double.parseDouble(gradeFieldString);
				if (gradeFieldValue > 0 && gradeFieldValue <= maxGrade) {
					Boolean gradeAssigned = course.assignGrade(student, gradeFieldValue);
					if (gradeAssigned) {
						gradeValue.setText(gradeFieldString);
					} else {
						gradeValue.setText("");
						formStatus.setText("Error occured. Grade not updated.");
					}
					submitBtn.setVisible(false);
					cancelBtn.setVisible(false);
					assignGradeBtn.setVisible(true);
					gradeField.setVisible(false);

				} else {

					formStatus.setText("Grade must be greater than 0 and less than maximum grade value.");
				}
			} catch (Exception err) {
				formStatus.setText("Grade must be a number");
				submitBtn.setDisable(false);
				return;
			}

			submitBtn.setDisable(false);
		});

		courseLine.getChildren().addAll(courseLabel, gradeValue, formStatus, gradeField, assignGradeBtn, submitBtn,
				cancelBtn);
		return courseLine;
	}

	private static void StudentEditForm(Student student) {
		VBox layout = new VBox(10);
		Label nameLabel = new Label("Name");
		TextField nameField = new TextField(student.getName());
		Label formStatus = new Label("");
		Button submitBtn = new Button("Submit");
		Button backBtn = new Button("Back");

		submitBtn.setOnAction(e -> {

			submitBtn.setDisable(true);
			String studentName = nameField.getText().trim();
			if (!studentName.isEmpty() && studentName != student.getName()) {

				student.setName(studentName);
				nameLabel.setVisible(false);
				nameField.setVisible(false);
				submitBtn.setVisible(false);
				formStatus.setText("Student name changed.");
			}

		});

		backBtn.setOnAction(e -> {
			showStudent(student);
		});
		layout.getChildren().addAll(nameLabel, nameField, formStatus, submitBtn, backBtn);

		primaryStage.setScene(new Scene(layout, width, height));

	}

	private static void showCourses(Course course) {
		VBox layout = new VBox(10);
		Label courseCodeLabel = new Label("Course code : ");
		TextField codeField = new TextField("");

		Label formStatus = new Label();

		Button searchBtn = new Button("Search");
		Button backBtn = new Button("Back");
		AtomicReference<HBox> courseToRenderRef = new AtomicReference<>(null);

		AtomicReference<String> lastSearchedCodeRef = new AtomicReference<>("");

		layout.getChildren().addAll(courseCodeLabel, codeField, formStatus, searchBtn, backBtn);
		int labelIndex = layout.getChildren().indexOf(formStatus);
		if (course != null) {
			lastSearchedCodeRef.set(String.format("%s", course.getCode()));
			courseToRenderRef.set(CourseComponent(course));
			codeField.setText(course.getCode());
			layout.getChildren().add(labelIndex + 1, courseToRenderRef.get());
		}
		backBtn.setOnAction(e -> {
			showMainMenu();
		});

		searchBtn.setOnAction(e -> {
			String codeString = codeField.getText().trim();

			if (codeString.equals(lastSearchedCodeRef.get())) {
				return;
			} else {
				lastSearchedCodeRef.set(codeString);
			}

			Course courseInside = Course.getCourse(codeString);
			if (courseInside != null) {
				if (courseToRenderRef.get() != null) {
					layout.getChildren().remove(courseToRenderRef.get());
				}
				courseToRenderRef.set(CourseComponent(courseInside));
				layout.getChildren().add(labelIndex + 1, courseToRenderRef.get());
			} else {
				formStatus.setText(String.format("Course with code `%s` not found.", codeString));
			}
		});

		primaryStage.setScene(new Scene(layout, width, height));

	}

	private static HBox CourseComponent(Course course) {
		HBox layout = new HBox(10);

		Label nameLabel = new Label("Name :");
		Label name = new Label(course.getName());
		Label codeLabel = new Label("Code: ");
		Label code = new Label(String.format("%s", course.getCode()));

		Button viewCourse = new Button("View Course");
		Button editCourse = new Button("Edit Course");
		Button enrollCourse = new Button("Enroll Student");

		editCourse.setOnAction(e -> {
			CourseEditForm(course);
		});

		enrollCourse.setOnAction(e -> {

			showEnrollStudentForm(course);
		});

		viewCourse.setOnAction(e -> {
			CourseDetailComponent(course);
		});

		layout.getChildren().addAll(nameLabel, name, codeLabel, code, viewCourse, editCourse, enrollCourse);
		return layout;

		// primaryStage.setScene(new Scene(layout, width, height));

	}

	private static void CourseDetailComponent(Course course) {
		VBox layout = new VBox(10);

		Label nameLabel = new Label("Name :");
		Label name = new Label(course.getName());

		Label courseCodeLabel = new Label("Course label: ");
		Label code = new Label(course.getCode());

		Label capacityLabel = new Label("Capacity: ");

		Label capacity = new Label(String.format("%d", course.getCapacity()));

		Label maxGradeLabel = new Label("Max grade: ");

		Label maxGrade = new Label(String.format("%d", course.getMaxGrade()));

		HBox nameLine = new HBox();

		Label studentsLabel = new Label("Students: ");

		nameLine.getChildren().addAll(nameLabel, name);

		HashMap<Integer, Double> students = course.getStudents();

		HBox studentInfoLayout = new HBox(10);
		Label studentNameLabel = new Label("Name");
		Label studentIDLabel = new Label("ID");

		Button backBtn = new Button("Back");

		studentInfoLayout.getChildren().addAll(studentNameLabel, studentIDLabel);

		layout.getChildren().addAll(nameLabel, name, courseCodeLabel, code, capacityLabel, capacity, maxGradeLabel,
				maxGrade, studentInfoLayout, backBtn);

		backBtn.setOnAction(e -> {
			showCourses(course);
		}); // VBox = new

		for (Map.Entry<Integer, Double> entry : students.entrySet()) {

			Integer studentID = entry.getKey();

			Student student = Student.getStudent(studentID);

			Label studentName = new Label(student.getName());

			Label studentIDElement = new Label(String.format("%d", studentID));

			HBox studentLine = new HBox(3);

			studentLine.getChildren().addAll(studentName, studentIDElement);

			int studentInfoLayoutIndex = layout.getChildren().indexOf(studentInfoLayout);

			layout.getChildren().add(studentInfoLayoutIndex + 1, studentLine);

		}

		primaryStage.setScene(new Scene(layout, width, height));

	}

	private static void CourseEditForm(Course course) {

		AtomicReference<String> courseNameValue = new AtomicReference<>(course.getName());

		AtomicReference<String> courseCodeValue = new AtomicReference<>(course.getCode());

		AtomicReference<Integer> capacityValue = new AtomicReference<>(course.getCapacity());

		AtomicReference<Integer> maxGradeValue = new AtomicReference<>(course.getMaxGrade());
		VBox layout = new VBox(10);
		Label nameLabel = new Label("Name");
		TextField nameField = new TextField(course.getName());
		Label nameFieldErr = new Label("");

		Label codeLabel = new Label("Course code");
		TextField codeField = new TextField(course.getCode());

		Label codeFieldErr = new Label("");

		Label capacityLabel = new Label("Course capacity");
		TextField capacityField = new TextField(String.format("%d", course.getCapacity()));

		Label capacityFieldErr = new Label("");

		Label maxGradeLabel = new Label("Max grade");

		TextField maxGradeField = new TextField(String.format("%d", course.getMaxGrade()));

		Label maxGradeFieldErr = new Label("");

		Label formStatus = new Label("");
		Button submitBtn = new Button("Submit");
		Button backBtn = new Button("Back");

		submitBtn.setOnAction(e -> {

			submitBtn.setDisable(true);
			String courseNameInput = nameField.getText().trim();
			String courseCodeInput = codeField.getText().trim();
			String capacityInput = capacityField.getText().trim();
			String maxGradeInput = maxGradeField.getText().trim();

			if (!courseNameInput.isEmpty() && courseNameInput != courseNameValue.get()) {

				Boolean courseNameSet = course.setName(courseNameInput);

				if (courseNameSet) {
					courseNameValue.set(courseNameInput);

					nameField.setText(courseNameInput);

				} else {
					nameFieldErr.setText("Course name must at least be grater than 5 characters.");
				}
				// nameLabel.setVisible(false);
				// nameField.setVisible(false);
				// submitBtn.setVisible(false);
			} else {
				if (courseNameInput.isEmpty()) {
					nameFieldErr.setText("Course name can't be empty");
				}
			}
			;

			if (!courseCodeInput.isEmpty() && courseCodeInput != courseCodeValue.get()) {

				course.setCode(courseCodeInput);
				courseCodeValue.set(courseCodeInput);
				codeField.setText(courseCodeInput);
				// nameLabel.setVisible(false);
				// nameField.setVisible(false);
				// submitBtn.setVisible(false);
			} else {
				if (courseCodeInput.isEmpty()) {
					codeFieldErr.setText("Course code can't be empty");
				}
			}
			;

			if (!capacityInput.isEmpty()) {

				int capacityInputInt;
				try {
					capacityInputInt = Integer.parseInt(capacityInput);
					if (capacityInputInt != capacityValue.get()) {
						boolean capacitySet = course.setCapacity(capacityInputInt);
						if (capacitySet) {
							capacityField.setText(capacityInput);
						} else {
							capacityFieldErr.setText("Capacity has to be greater than 0.");
						}

					}
				} catch (Exception err) {
					capacityFieldErr.setText("Capacity has to be a number.");
				}
			} else {
				capacityFieldErr.setText("Capacity can't be empty.");
			}

			if (!maxGradeInput.isEmpty()) {

				int maxGradeInputInt;
				try {
					maxGradeInputInt = Integer.parseInt(maxGradeInput);
					if (maxGradeInputInt != maxGradeValue.get()) {
						Boolean maxGradeSet = course.setMaxGrade(maxGradeInputInt);
						if (maxGradeSet) {
							maxGradeField.setText(maxGradeInput);
						} else {
							maxGradeFieldErr.setText("Max grade must be greater than 0 and less than 100");
						}

					}
				} catch (Exception err) {
					maxGradeFieldErr.setText("Max grade has to be a number.");
				}
			} else {
				maxGradeFieldErr.setText("Max grade can't be empty.");
			}

		});

		backBtn.setOnAction(e -> {
			showCourses(course);
		});
		layout.getChildren().addAll(nameLabel, nameField, nameFieldErr, codeLabel, codeField, codeFieldErr, capacityLabel,
				capacityField, capacityFieldErr, maxGradeLabel, maxGradeField, maxGradeFieldErr, formStatus, submitBtn,
				backBtn);

		primaryStage.setScene(new Scene(layout, width, height));

	}

	private static void showEnrollStudentForm(Course course) {
		VBox layout = new VBox(10);
		Label courseCodeLabel = new Label("Course code : ");
		TextField codeField = new TextField(course.getCode());
		codeField.setDisable(true);

		Label studentIDLabel = new Label("Student ID : ");
		TextField idField = new TextField();

		Label courseCodeErr = new Label("");
		Label studentIDErr = new Label("");
		Label formStatus = new Label("");

		Button submitBtn = new Button("Submit");
		Button backBtn = new Button("Back");

		submitBtn.setOnAction(e -> {
			String studentIDStr = idField.getText().trim();
			int studentIDInt;
			if (!studentIDStr.isEmpty()) {

				try {
					studentIDInt = Integer.parseInt(studentIDStr);

					Student student = Student.getStudent(studentIDInt);

					if (course != null && student != null) {
						Boolean enrolledStudent = course.enrollStudent(student);
						if (enrolledStudent) {
							studentIDLabel.setDisable(true);
							idField.setDisable(true);
							courseCodeLabel.setDisable(true);
							codeField.setDisable(true);
							submitBtn.setDisable(true);
							courseCodeErr.setText("");
							studentIDErr.setText("");
							formStatus.setText("Student enrolled sucessfully.");
						}
					} else {
						if (course == null) {
							courseCodeErr.setText(String.format("Student `%s` doesn't exist.", studentIDStr));
						} else {
							formStatus.setText("Student couldn't be enrolled");
						}
					}
				} catch (Exception err) {
					studentIDErr.setText("Student ID cant be empty and must be a number.");
				}

			}
		});
		backBtn.setOnAction(e -> {
			studentIDLabel.setDisable(false);
			idField.setDisable(false);
			courseCodeLabel.setDisable(false);
			codeField.setDisable(false);
			submitBtn.setDisable(false);
			formStatus.setText("");
			showCourses(course);
		});

		layout.getChildren().addAll(studentIDLabel, idField, studentIDErr, courseCodeLabel, codeField, courseCodeErr,
				formStatus,
				submitBtn, backBtn);

		primaryStage.setScene(new Scene(layout, width, height));
	}

	// private List<Nodes> StudentCourses(Student student){
	// List<Course> courses = student.getCourses();
	//
	// List<Nodes> courseNodes = [];
	// for(Map.Entry<Course> entry: course){
	// Label courseCode = new Label(course.getCode());
	// Label courseGrade =
	// }
	// }

}

class Student {

	private static HashMap<Integer, Student> students = new HashMap<>();
	private static int studentCount = 0;
	private HashMap<String, Double> courses = new HashMap<>();
	private String name;
	private Integer id;

	Student(String name) {
		this.name = name;

		if (studentCount == 0) {
			this.id = 0;
			studentCount += 1;

		} else {
			studentCount += 1;
			this.id = studentCount;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void assignGrade(String courseCode, Double grade) {
		courses.put(courseCode, grade);
	}

	public static Student createStudent(String name) {
		// for (Map.Entry<Integer, Student> entry : students.entrySet()) {
		// // System.out.println(entry.getKey() + " -> " + entry.getValue());
		//
		// if (entry.getValue().name.toLowerCase() == name){
		// return
		// }
		Student student = new Student(name);
		students.put(student.getId(), student);
		return student;
	}

	// public static getStudents()

	public static boolean addCourse(Student student, Course course) {
		if (!student.isEnrolledToCourse(course)) {
			student.courses.put(course.getCode(), 0.0);
			return true;
		}
		return false;
	}

	public boolean addCourse(Course course) {
		if (!this.isEnrolledToCourse(course)) {
			this.courses.put(course.getCode(), 0.0);
			return true;
		}
		return false;
	}

	public boolean removeCourse(Course course) {
		if (this.isEnrolledToCourse(course)) {
			this.courses.remove(course.getCode());
			return true;
		}
		return false;
	}

	public boolean removeCourse(String courseCode) {
		if (this.isEnrolledToCourse(courseCode)) {
			this.courses.remove(courseCode);
			return true;
		}
		return false;
	}

	public boolean isEnrolledToCourse(Course course) {
		if (this.courses.get(course.getCode()) != null) {
			return true;
		}
		return false;
	}

	public static Student getStudent(Integer id) {
		return students.get(id);
	}

	public HashMap<String, Double> getCourses(Integer startIndex, Integer endIndex) {
		if (startIndex > this.courses.size() - 1) {
			return null;
		}
		if (this.courses.size() < endIndex) {
			endIndex = this.courses.size() - 1;
		}
		HashMap<String, Double> studentCourses = new HashMap<>();
		int count = 0;
		for (Map.Entry<String, Double> entry : courses.entrySet()) {
			if (count == startIndex && count <= endIndex) {
				studentCourses.put(entry.getKey(), entry.getValue());
			}
		}

		return this.courses;
	}

	public HashMap<String, Double> getCourses() {

		return this.courses;
	}

	public boolean isEnrolledToCourse(String courseCode) {
		if (this.courses.get(courseCode) != null) {
			return true;
		}
		return false;
	}

	public int getId() {
		return this.id;
	}

}

class Course {
	private static HashMap<String, Course> courses = new HashMap<>();
	private HashMap<Integer, Double> enrolledStudents = new HashMap<>();

	private String name;
	private String code;
	private Integer maxGrade;
	private Integer capacity;

	private Course(String name, String code, Integer capacity, Integer maxGrade) {
		this.name = name;
		this.code = code;
		this.maxGrade = maxGrade;
		this.capacity = capacity;
		courses.put(code, this);
	}

	// public Course addCourse(String name, String code) {
	// if (courseExists(code)) {
	// return null;
	// }
	// return new Course(name, code);
	// }

	public String getCode() {
		return this.code;
	}

	public Boolean setCode(String code) {
		if (code.length() > 3) {
			return true;
		}
		return false;
	}

	public Integer getMaxGrade() {
		return this.maxGrade;
	}

	public Integer getCapacity() {
		return this.capacity;
	}

	public Boolean setCapacity(Integer capacity) {
		if (capacity > 0) {
			this.capacity = capacity;
			return true;
		}
		return false;
	}

	public Boolean setMaxGrade(Integer maxGrade) {
		if (maxGrade > 0 & maxGrade <= 100) {
			this.maxGrade = maxGrade;
			return true;
		}

		return false;
	}

	public HashMap<Integer, Double> getStudents() {
		return enrolledStudents;
	}

	public Boolean setName(String name) {
		if (name.length() > 5) {
			this.name = name;
			return true;
		}
		return false;
	}

	public boolean isStudentEnrolled(Student student) {
		if (enrolledStudents.get(student.getId()) != null) {
			return true;
		}
		return false;
	}

	public boolean enrollStudent(Student student) {
		if (!this.isStudentEnrolled(student) && enrolledStudents.size() < this.getCapacity()) {
			enrolledStudents.put(student.getId(), 0.0);
			student.addCourse(this);
			return true;
		}
		return false;
	}

	public boolean assignGrade(Student student, Double grade) {
		if (isStudentEnrolled(student) && grade >= 0 && grade <= this.getMaxGrade()) {
			enrolledStudents.put(student.getId(), grade);
			student.assignGrade(this.getCode(), grade);
			return true;
		}
		return false;
	}

	public static Course getCourse(String code) {
		if (courseExists(code)) {
			return courses.get(code);
		}
		return null;
	}

	public boolean cancelCourse(Student student) {
		if (this.isStudentEnrolled(student)) {
			enrolledStudents.remove(student.getId());
			student.removeCourse(this.code);
			return true;
		}
		return false;
	}

	public static boolean addCourse(String name, String code, Integer capacity, Integer maxGrade) {
		if (courseExists(code)) {
			return false;
		}
		new Course(name, code, capacity, maxGrade);
		return true;
	}

	public String getName() {
		return this.name;
	}

	public double getGrade(Student student) {
		if (isStudentEnrolled(student)) {
			double grade = enrolledStudents.get(student.getId());
			return grade;
		}
		return -0.0;
	}

	public static boolean courseExists(String code) {

		if (courses.containsKey(code)) {
			return true;
		}
		return false;
	}

}
