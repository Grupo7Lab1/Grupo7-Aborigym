-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 21-10-2026 a las 02:00:13
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `g7_gimnasio_ aborigym`
--
CREATE DATABASE IF NOT EXISTS `g7_gimnasio_ aborigym` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `g7_gimnasio_ aborigym`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asistencia`
--

CREATE TABLE `asistencia` (
  `ID_Asistencia` int(11) NOT NULL,
  `ID_Socio` int(11) NOT NULL,
  `ID_Clase` int(11) NOT NULL,
  `Fecha_Asistencia` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asistencia`
--

INSERT INTO `asistencia` (`ID_Asistencia`, `ID_Socio`, `ID_Clase`, `Fecha_Asistencia`) VALUES
(1, 1, 1, '2026-10-09'),
(2, 1, 3, '2026-10-11'),
(3, 1, 1, '2026-10-14'),
(4, 1, 3, '2026-10-16'),
(5, 1, 1, '2026-10-18'),
(6, 2, 5, '2026-10-16'),
(7, 2, 3, '2026-10-17'),
(8, 2, 5, '2026-10-18'),
(9, 2, 4, '2026-10-19'),
(10, 3, 5, '2026-10-10'),
(11, 3, 5, '2026-10-11'),
(12, 3, 5, '2026-10-12'),
(13, 3, 5, '2026-10-13'),
(14, 3, 4, '2026-10-14'),
(15, 3, 5, '2026-10-15'),
(16, 3, 5, '2026-10-16'),
(17, 3, 4, '2026-10-17'),
(18, 7, 7, '2026-08-18'),
(19, 7, 7, '2026-08-21'),
(20, 7, 7, '2026-08-24'),
(21, 7, 7, '2026-08-27'),
(22, 7, 7, '2026-08-30'),
(23, 7, 7, '2026-09-02'),
(24, 7, 7, '2026-09-05'),
(25, 7, 7, '2026-09-08'),
(26, 7, 7, '2026-10-06'),
(27, 7, 4, '2026-10-10'),
(28, 7, 7, '2026-10-15'),
(29, 9, 6, '2026-10-07'),
(30, 9, 6, '2026-10-09'),
(31, 9, 6, '2026-10-11'),
(32, 9, 6, '2026-10-13'),
(33, 9, 6, '2026-10-15'),
(34, 9, 6, '2026-10-17'),
(35, 9, 6, '2026-10-19'),
(36, 10, 2, '2026-09-17'),
(37, 10, 2, '2026-09-20'),
(38, 10, 2, '2026-09-23'),
(39, 10, 2, '2026-09-27'),
(40, 10, 2, '2026-09-30');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clasegym`
--

CREATE TABLE `clasegym` (
  `ID_Clase` int(11) NOT NULL,
  `Disciplina` varchar(50) NOT NULL,
  `ID_Entrenador` int(11) NOT NULL,
  `Horario` time NOT NULL,
  `Capacidad` int(11) NOT NULL,
  `Estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clasegym`
--

INSERT INTO `clasegym` (`ID_Clase`, `Disciplina`, `ID_Entrenador`, `Horario`, `Capacidad`, `Estado`) VALUES
(1, 'Pilates', 1, '09:00:00', 10, 1),
(2, 'Pilates', 1, '18:00:00', 10, 1),
(3, 'Spinning', 2, '10:00:00', 15, 1),
(4, 'Spinning', 2, '19:00:00', 15, 1),
(5, 'CrossFit', 3, '08:00:00', 12, 1),
(6, 'CrossFit', 3, '17:00:00', 2, 1),
(7, 'Musculacion', 4, '20:00:00', 20, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrenador`
--

CREATE TABLE `entrenador` (
  `ID_Entrenador` int(11) NOT NULL,
  `DNI` varchar(15) NOT NULL,
  `Nombre` varchar(50) NOT NULL,
  `Apellido` varchar(50) NOT NULL,
  `Especialidad` varchar(50) NOT NULL,
  `Estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `entrenador`
--

INSERT INTO `entrenador` (`ID_Entrenador`, `DNI`, `Nombre`, `Apellido`, `Especialidad`, `Estado`) VALUES
(1, '25111000', 'Mariela ', 'Saez', 'Pilates', 1),
(2, '26222000', 'Andrea', 'Gonzalez, Sanabria', 'Spinning', 1),
(3, '27333000', 'Hugo Ezequiel', 'Carreño', 'CrossFit', 1),
(4, '29444000', 'Juan jose', 'Saez', 'Musculacion', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pase`
--

CREATE TABLE `pase` (
  `ID_Pase` int(11) NOT NULL,
  `ID_Socio` int(11) NOT NULL,
  `CantidadEntradas` int(11) NOT NULL,
  `Fecha_Inicio` date NOT NULL,
  `Fecha_Fin` date NOT NULL,
  `Costo` decimal(10,2) NOT NULL,
  `Estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pase`
--

INSERT INTO `pase` (`ID_Pase`, `ID_Socio`, `CantidadEntradas`, `Fecha_Inicio`, `Fecha_Fin`, `Costo`, `Estado`) VALUES
(1, 1, 7, '2026-10-08', '2026-11-07', 25000.00, 1),
(2, 2, 16, '2026-10-16', '2026-11-15', 35000.00, 1),
(3, 3, 0, '2026-10-10', '2026-11-09', 18000.00, 1),
(4, 4, 8, '2026-10-20', '2026-11-19', 18000.00, 1),
(5, 5, 12, '2026-09-10', '2026-10-10', 25000.00, 1),
(6, 7, 0, '2026-08-16', '2026-09-15', 18000.00, 0),
(7, 7, 9, '2026-10-05', '2026-11-04', 25000.00, 1),
(8, 8, 8, '2026-08-31', '2026-09-30', 18000.00, 0),
(9, 9, 1, '2026-10-06', '2026-11-05', 18000.00, 1),
(10, 10, 3, '2026-09-15', '2026-10-15', 18000.00, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `socio`
--

CREATE TABLE `socio` (
  `ID_Socio` int(11) NOT NULL,
  `DNI` varchar(15) NOT NULL,
  `Nombre` varchar(50) NOT NULL,
  `Apellido` varchar(50) NOT NULL,
  `Edad` int(11) NOT NULL,
  `Correo` varchar(100) NOT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  `Estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `socio`
--

INSERT INTO `socio` (`ID_Socio`, `DNI`, `Nombre`, `Apellido`, `Edad`, `Correo`, `Telefono`, `Estado`) VALUES
(1, '30111222', 'Lucas', 'Gomez', 28, 'lucas.gomez@mail.com', '2657-111111', 1),
(2, '32222333', 'Martina', 'Perez', 24, 'martina.perez@mail.com', '2657-222222', 1),
(3, '28333444', 'Carlos', 'Rodriguez', 35, 'carlos.rodriguez@mail.com', '2657-333333', 1),
(4, '40444555', 'Sofia', 'Lopez', 21, 'sofia.lopez@mail.com', '2657-444444', 1),
(5, '35555666', 'Julian', 'Fernandez', 30, 'julian.fernandez@mail.com', '2657-555555', 1),
(6, '41666777', 'Valentina', 'Ruiz', 19, 'valentina.ruiz@mail.com', '2657-666666', 1),
(7, '33777888', 'Mateo', 'Castro', 27, 'mateo.castro@mail.com', '2657-777777', 1),
(8, '36888999', 'Camila', 'Herrera', 26, 'camila.herrera@mail.com', '2657-888888', 0),
(9, '29999000', 'Nicolas', 'Acosta', 33, 'nicolas.acosta@mail.com', '2657-999999', 1),
(10, '38000111', 'Agustina', 'Medina', 23, 'agustina.medina@mail.com', '2657-000000', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asistencia`
--
ALTER TABLE `asistencia`
  ADD PRIMARY KEY (`ID_Asistencia`),
  ADD KEY `fk_asist_socio` (`ID_Socio`),
  ADD KEY `fk_asist_clase` (`ID_Clase`);

--
-- Indices de la tabla `clasegym`
--
ALTER TABLE `clasegym`
  ADD PRIMARY KEY (`ID_Clase`),
  ADD KEY `fk_clase_entrenador` (`ID_Entrenador`);

--
-- Indices de la tabla `entrenador`
--
ALTER TABLE `entrenador`
  ADD PRIMARY KEY (`ID_Entrenador`),
  ADD UNIQUE KEY `uq_entrenador_dni` (`DNI`);

--
-- Indices de la tabla `pase`
--
ALTER TABLE `pase`
  ADD PRIMARY KEY (`ID_Pase`),
  ADD KEY `fk_pase_socio` (`ID_Socio`);

--
-- Indices de la tabla `socio`
--
ALTER TABLE `socio`
  ADD PRIMARY KEY (`ID_Socio`),
  ADD UNIQUE KEY `uq_socio_dni` (`DNI`),
  ADD UNIQUE KEY `uq_socio_correo` (`Correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `asistencia`
--
ALTER TABLE `asistencia`
  MODIFY `ID_Asistencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT de la tabla `clasegym`
--
ALTER TABLE `clasegym`
  MODIFY `ID_Clase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `entrenador`
--
ALTER TABLE `entrenador`
  MODIFY `ID_Entrenador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `pase`
--
ALTER TABLE `pase`
  MODIFY `ID_Pase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `socio`
--
ALTER TABLE `socio`
  MODIFY `ID_Socio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asistencia`
--
ALTER TABLE `asistencia`
  ADD CONSTRAINT `fk_asist_clase` FOREIGN KEY (`ID_Clase`) REFERENCES `clasegym` (`ID_Clase`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_asist_socio` FOREIGN KEY (`ID_Socio`) REFERENCES `socio` (`ID_Socio`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `clasegym`
--
ALTER TABLE `clasegym`
  ADD CONSTRAINT `fk_clase_entrenador` FOREIGN KEY (`ID_Entrenador`) REFERENCES `entrenador` (`ID_Entrenador`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `pase`
--
ALTER TABLE `pase`
  ADD CONSTRAINT `fk_pase_socio` FOREIGN KEY (`ID_Socio`) REFERENCES `socio` (`ID_Socio`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
