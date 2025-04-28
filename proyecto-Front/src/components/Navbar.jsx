import React, { useState, useEffect } from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography"; 
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import Sidemenu from "./Sidemenu";
import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {
  const [usuario, setUsuario] = useState(null);
  const [loading, setLoading] = useState(true);

  const [open, setOpen] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUsuario = localStorage.getItem("usuario");
    console.log(storedUsuario);
    if (storedUsuario) {
      setUsuario(JSON.parse(storedUsuario));
    }
    setLoading(false);
  }, []);

  const toggleDrawer = (open) => () => {
    setOpen(open);
  };

  const handleLogout = () => {
    localStorage.removeItem("usuario");
    setUsuario(null);
    navigate("/login");
  };

  if (loading) {
    return null; 
  }

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
            onClick={toggleDrawer(true)}
          >
            <MenuIcon />
          </IconButton>

          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Sistema de Karting RM.
          </Typography>

          {!usuario ? (
            <Link to="/login">
              <Button sx={{ color: "#fff" }}>Iniciar sesión</Button>
            </Link>
          ) : (
            <>
              <Typography variant="h6" color="inherit" sx={{ mr: 2 }}>
                {usuario.nombre}
              </Typography>
              <Button sx={{ color: "#fff" }} onClick={handleLogout}>
                Cerrar sesión
              </Button>
            </>
          )}  
        </Toolbar>
      </AppBar>

      <Sidemenu open={open} toggleDrawer={toggleDrawer} />
    </Box>
  );
}
