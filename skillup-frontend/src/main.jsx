import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import './styles/theme.css';
import SignIn from './pages/general/SignIn.jsx';
import Landing from './pages/general/Landing.jsx';
import Dashboard from './pages/general/Dashboard.jsx';
import DashboardLayout from './components/DashboardLayout.jsx';
import Profile from './pages/general/Profile.jsx';
import StudentDashboard from './pages/student/StudentDashboard.jsx';
import StudentAchievements from './pages/student/StudentAchievements.jsx';
import StudentLeaderboard from './pages/student/StudentLeaderboard.jsx';
import ActivityWork from './pages/student/ActivityWork.jsx';
import ProfessorDashboard from './pages/professor/ProfessorDashboard.jsx';
import ProfessorAssignments from './pages/professor/ProfessorAssignments.jsx';
import ProfessorGrading from './pages/professor/ProfessorGrading.jsx';
import ProfessorCodes from './pages/professor/ProfessorCodes.jsx';
import ProfessorLive from './pages/professor/ProfessorLive.jsx';
import ProfessorExercises from './pages/professor/ProfessorExercises.jsx';
import AdminDashboard from './pages/admin/AdminDashboard.jsx';
import AdminUsers from './pages/admin/AdminUsers.jsx';
import AdminReports from './pages/admin/AdminReports.jsx';
import { AuthProvider, useAuth } from './services/AuthContext.jsx';

function ProtectedRoute({ children }) {
  const { token } = useAuth();
  if (!token) return <Navigate to="/login" replace />;
  return children;
}

function RoleRoute({ allow, children }) {
  const { user, token } = useAuth();
  if (!token) return <Navigate to="/login" replace />;
  const normalizedAllow = (allow || []).map(role => role.toUpperCase());
  const userRoles = (user?.roles || []).map(role => (role.name || '').toUpperCase());
  const isAllowed = normalizedAllow.some(role => userRoles.includes(role));
  if (!user) {
    return <div className="p-4">Cargando rol...</div>;
  }
  if (!isAllowed) return <Navigate to="/" replace />;
  return children;
}

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<SignIn />} />
          <Route element={<ProtectedRoute><DashboardLayout /></ProtectedRoute>}>
            <Route index element={<Dashboard />} />

            <Route path="student">
              <Route index element={<Navigate to="dashboard" replace />} />
              <Route
                path="dashboard"
                element={(
                  <RoleRoute allow={["STUDENT", "ROLE_STUDENT", "ESTUDIANTE"]}>
                    <StudentDashboard />
                  </RoleRoute>
                )}
              />
              <Route
                path="achievements"
                element={(
                  <RoleRoute allow={["STUDENT", "ROLE_STUDENT", "ESTUDIANTE"]}>
                    <StudentAchievements />
                  </RoleRoute>
                )}
              />
              <Route
                path="leaderboard"
                element={(
                  <RoleRoute allow={["STUDENT", "ROLE_STUDENT", "ESTUDIANTE"]}>
                    <StudentLeaderboard />
                  </RoleRoute>
                )}
              />
              <Route
                path="activity/:id"
                element={(
                  <RoleRoute allow={["STUDENT", "ROLE_STUDENT", "ESTUDIANTE"]}>
                    <ActivityWork />
                  </RoleRoute>
                )}
              />
            </Route>

            <Route path="professor">
              <Route index element={<Navigate to="dashboard" replace />} />
              <Route
                path="dashboard"
                element={(
                  <RoleRoute allow={["PROFESSOR", "ROLE_PROFESSOR", "TEACHER", "PROFESOR"]}>
                    <ProfessorDashboard />
                  </RoleRoute>
                )}
              />
              <Route
                path="assignments"
                element={(
                  <RoleRoute allow={["PROFESSOR", "ROLE_PROFESSOR", "TEACHER", "PROFESOR"]}>
                    <ProfessorAssignments />
                  </RoleRoute>
                )}
              />
              <Route
                path="exercises"
                element={(
                  <RoleRoute allow={["PROFESSOR", "ROLE_PROFESSOR", "TEACHER", "PROFESOR"]}>
                    <ProfessorExercises />
                  </RoleRoute>
                )}
              />
              <Route
                path="grading"
                element={(
                  <RoleRoute allow={["PROFESSOR", "ROLE_PROFESSOR", "TEACHER", "PROFESOR"]}>
                    <ProfessorGrading />
                  </RoleRoute>
                )}
              />
              <Route
                path="codes"
                element={(
                  <RoleRoute allow={["PROFESSOR", "ROLE_PROFESSOR", "TEACHER", "PROFESOR"]}>
                    <ProfessorCodes />
                  </RoleRoute>
                )}
              />
              <Route
                path="live"
                element={(
                  <RoleRoute allow={["PROFESSOR", "ROLE_PROFESSOR", "TEACHER", "PROFESOR"]}>
                    <ProfessorLive />
                  </RoleRoute>
                )}
              />
            </Route>

            <Route path="admin">
              <Route index element={<Navigate to="dashboard" replace />} />
              <Route
                path="dashboard"
                element={(
                  <RoleRoute allow={["ADMIN", "ROLE_ADMIN", "ADMINISTRADOR"]}>
                    <AdminDashboard />
                  </RoleRoute>
                )}
              />
              <Route
                path="users"
                element={(
                  <RoleRoute allow={["ADMIN", "ROLE_ADMIN", "ADMINISTRADOR"]}>
                    <AdminUsers />
                  </RoleRoute>
                )}
              />
              <Route
                path="reports"
                element={(
                  <RoleRoute allow={["ADMIN", "ROLE_ADMIN", "ADMINISTRADOR"]}>
                    <AdminReports />
                  </RoleRoute>
                )}
              />
            </Route>

            <Route path="profile" element={<Profile />} />
          </Route>
          <Route path="/landing" element={<ProtectedRoute><Landing /></ProtectedRoute>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  </React.StrictMode>
);