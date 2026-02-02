# 🏥 Hospital Management System (HMS)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-blue?style=for-the-badge)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)

A professional, modern Hospital Management System built with **JavaFX** and **SQLite**, featuring role-based portals for doctors and patients with real-time appointment booking and digital prescription management.

---

## ✨ Project Overview
This HMS project is designed to digitize hospital workflows. By utilizing **Custom Doubly Linked Lists** for data management and **SOLID principles** for architecture, it ensures high performance and maintainability. It supports two primary user roles: **Doctors** and **Patients**, each with a dedicated suite of tools.

---

## 🖼️ Application Walkthrough

### 1. Authentication & Role Selection
The entry point allows users to choose their specialized portal and authenticate securely.

| 🎭 Role Selection | 🔑 Login Interface | 📝 Secure Sign-Up |
| :---: | :---: | :---: |
| ![Role Selection](./Screenshots/Role%20Selection.jpg) | ![Login Screen](./Screenshots/Login%20Interface.jpg) | ![Secure Sign-Up](./Screenshots/Secure%20Sign-Up.jpg) |
| *Toggle between portals.* | *Secure credential check.* | *New user registration.* |

---

### 👨‍⚕️ 2. Doctor Portal (Clinical Workflow)

| 🏠 Doctor Dashboard | 👥 Assigned Patients | 💊 Digital Prescription |
| :---: | :---: | :---: |
| ![Doctor Home](./Screenshots/Doctor%20Dashboard.jpg) | ![Patient Queue](./Screenshots/Assigned%20Patients.jpg) | ![Prescription Tool](./Screenshots/Digital%20Prescription.jpg) |

| 📊 Schedule Analytics | 📂 Global Patient Registry | ⚙️ Slot Management |
| :---: | :---: | :---: |
| ![Analytics](./Screenshots/Schedule%20Analytics.jpg) | ![All Patients](./Screenshots/Global%20Patient%20Registry.jpg) | ![Slot Setup](./Screenshots/Slot%20Management.jpg) |

| 👨‍⚕️ Professional Profile |
| :---: |
| ![Doctor Profile](./Screenshots/Professional%20Profile.jpg) |

---

### 🧍 3. Patient Portal (Healthcare Journey)

| 🏠 Patient Dashboard | 📅 Appointment Booking | ✅ Booking Success |
| :---: | :---: | :---: |
| ![Patient Home](./Screenshots/Patient%20Dashboard.jpg) | ![Booking Screen](./Screenshots/Appointment%20Booking.jpg) | ![Success](./Screenshots/Booking%20Confirmation.jpg) |

| 📜 Appointment History | 📄 Prescription View | 👨‍⚕️ Available Doctors |
| :---: | :---: | :---: |
| ![History](./Screenshots/Appointment%20History.jpg) | ![Prescription Details](./Screenshots/Digital%20Prescription%20View.jpg) | ![Doctor List](./Screenshots/Available%20Doctors.jpg) |

---

### 🛠️ 4. Technical Backend & Logs
| 🖥️ Developer Console & Logs |
| :---: |
| ![System Logs](./Screenshots/Developer%20Console%20%26%20Database%20Logs.jpg) |

---

## 💻 Tech Stack
* **Language:** Java 11+
* **Frontend:** JavaFX (CSS for styling)
* **Database:** SQLite (JDBC)
* **Architecture:** MVC (Model-View-Controller)
* **Data Structures:** Custom Doubly Linked Lists for $O(1)$ operations.

## 🚀 Key Technical Features

* **Priority Queue Logic:** Patients are automatically sorted by medical urgency (Emergency > Intermediate > Normal).
* **Dynamic Slot System:** Automated slot availability updates upon booking/completion.
* **Persistent Storage:** All records (Users, Appointments, Slots) are stored in a relational SQLite database.

---
© 2026 Hifza & Tahir - All Rights Reserved.
