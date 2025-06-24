# Flight Manager ✈️

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=spring&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-19-DD0031?style=flat-square&logo=angular&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-38B2AC?style=flat-square&logo=tailwind-css&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat-square&logo=postgresql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white)

A flight management system that enables efficient flight management and ticket sales.

## 📋 Description

Flight Manager is a full-stack web application developed to facilitate flight management. The application provides an intuitive interface for searching through existing routes with expansion plans for a complete booking system and user management.

## 🖼️ Screenshots

<div align="center">
  <img src="https://github.com/user-attachments/assets/56e77ec2-b2af-4a17-be4f-aa4c82fbfb41" alt="Flight Search Form" width="500" height="229" style="border-radius: 8px; object-fit: cover; margin: 5px;" />
  <img src="https://github.com/user-attachments/assets/a229df02-2ba1-4c73-87ae-30f2653b30fd" alt="Custom Calendar" width="500" height="229" style="border-radius: 8px; object-fit: cover; margin: 5px;" />
  <img src="https://github.com/user-attachments/assets/8121cced-c472-4f7c-9bff-32b3d3be89d1" alt="Custom Calendar" width="250" height="541" style="border-radius: 8px; object-fit: cover; margin: 5px;" />
  <img src="https://github.com/user-attachments/assets/8671c851-601f-460f-8a3f-abd9bdc20001" alt="Custom Calendar" width="250" height="505" style="border-radius: 8px; object-fit: cover; margin: 5px;" />
</div>
<div align="center">
</div>

## 🚀 Features

### Implemented
- ✅ **Flight Viewing** - Complete list of all available flights
- ✅ **Flight Addition** - Create new flights with complete details
- ✅ **Flight Modification** - Update existing flight information
- ✅ **Flight Deletion** - Remove flights from the system

### In Development
- 🔄 **Ticket Purchasing** - Booking and ticket purchase system
- 🔄 **Authentication System** - User login functionality
- 🔄 **Role Management** - Separation into User/Agency/Admin roles

## 🏗️ Architecture

The project is structured as a full-stack application with separate frontend and backend:

```
flight-manager/
├── backend/          # Spring Boot API
└── frontend/         # Angular application
```

## 🛠️ Technology Stack

### Backend
- **Framework**: Java Spring Boot
- **Build Tool**: Gradle
- **Database**: PostgreSQL

### Frontend
- **Framework**: Angular 19
- **Language**: TypeScript
- **Styling**: Tailwind CSS

## 📦 Installation & Setup

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 12+
- Angular CLI 19

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Configure database connection in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:your_port/your_db_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run the application:
```bash
./gradlew bootRun
```

The backend API will be available at `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
ng serve
```

The frontend application will be available at `http://localhost:4200`

### Database Setup

1. Create a PostgreSQL database named `flight_manager`
2. The application will automatically create the necessary tables on first run

## 🎨 Styling

The frontend uses **Tailwind CSS** for utility-first styling, providing:
- Responsive design out of the box
- Consistent color palette and spacing
- Fast development with utility classes
- Modern, clean UI components

## 🔧 API Endpoints

### Flights
- `GET /travel/flights` - Get all flights
- `POST /travel/flights` - Create a new flight
- `GET /travel/flights/{id}` - Get flight by ID
- `PUT /travel/flights/{id}` - Update flight
- `DELETE /travel/flights/{id}` - Delete flight

## 🚧 Development Roadmap

### Phase 1 (Current)
- [x] Basic CRUD operations for flights
- [x] RESTful API implementation
- [x] Angular frontend interface
- [x] Tailwind CSS integration

### Phase 2 (Next)
- [ ] Ticket booking system
- [ ] Payment integration (simulation)
- [ ] User authentication

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
