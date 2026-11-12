\# Sistema de Gestión de Gimnasio - API REST (Grupo 11)



Proyecto grupal correspondiente a la Unidad 1 de la asignatura \*\*Programación Avanzada\*\* (NRC 36759) de la Universidad de las Fuerzas Armadas ESPE.



\## Descripción del Caso de Estudio

Desarrollo de una API empresarial con arquitectura N capas para administrar la operación de un gimnasio, permitiendo gestionar clientes, planes de membresía, entrenadores y el control de asistencias con validación automática de vigencia.



\## Integrantes del Grupo 11

\* \*\*Integrante 1:\*\* Jersson Kevin Vera Rivas - \*Base de Datos, Entidades JPA, Repositorios y Pipeline CI/CD\*

\* \*\*Integrante 2:\*\* Sarai Eli Panta Ayala - \*Lógica de Negocio (Service), DTOs y Reglas de Dominio\*

\* \*\*Integrante 3:\*\* Darío Patricio Chuga Terán - \*Controladores REST, Manejo Centralizado de Excepciones y Middleware HTTP\*

\* \*\*Integrante 4:\*\* Sebastián Alejandro Simba Onofa - \*Programación Orientada a Aspectos (AOP), Procesos Asíncronos y Pruebas\*



\## Arquitectura y Tecnologías Utilizadas

\* \*\*Lenguaje:\*\* Java 21+

\* \*\*Framework:\*\* Spring Boot (Spring Web, Spring Data JPA, Spring AOP, Bean Validation)

\* \*\*Base de Datos:\*\* Relacional (PostgreSQL / MySQL)

\* \*\*Arquitectura:\*\* N Capas (`entity`, `repository`, `dto`, `service`, `controller`, `exception`, `interceptor`, `aspect`, `config`)

\* \*\*DevOps:\*\* Git, GitHub y Pipeline de Integración Continua con GitHub Actions



\## Endpoints Principales



| Método | Endpoint | Descripción |

| :--- | :--- | :--- |

| `GET` | `/api/clientes` | Listar todos los clientes registrados |

| `GET` | `/api/clientes/{id}` | Consultar cliente por identificador |

| `GET` | `/api/clientes/membresia-vencida` | Consultar clientes con membresía caducada |

| `POST` | `/api/clientes` | Registrar un nuevo cliente |

| `PUT` | `/api/clientes/{id}` | Actualizar datos de un cliente |

| `DELETE` | `/api/clientes/{id}` | Eliminar un cliente |

| `GET` | `/api/planes` | Listar planes disponibles |

| `POST` | `/api/planes` | Crear un nuevo plan de membresía |

| `PATCH` | `/api/planes/{id}/precio` | Actualizar el precio de un plan |

| `DELETE` | `/api/planes/{id}` | Eliminar un plan |

| `GET` | `/api/asistencias/cliente/{clienteId}` | Consultar asistencias por cliente |

| `POST` | `/api/asistencias` | Registrar asistencia validando vigencia de membresía |



\## Ejecución del Proyecto

1\. Configurar las credenciales de base de datos en `src/main/resources/application.properties`.

2\. Compilar el proyecto:

&#x20;  ```bash

&#x20;  mvn clean compile

