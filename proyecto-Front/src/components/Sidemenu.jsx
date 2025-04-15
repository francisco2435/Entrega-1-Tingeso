import * as React from "react";
import Box from "@mui/material/Box";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import DirectionsCarIcon from "@mui/icons-material/DirectionsCar";
import BarChartIcon from "@mui/icons-material/BarChart";
import DateRangeIcon from "@mui/icons-material/DateRange";
import InsertDriveFileIcon from "@mui/icons-material/InsertDriveFile";
import CalendarTodayIcon from "@mui/icons-material/CalendarToday";
import HomeIcon from "@mui/icons-material/Home";
import { useNavigate } from "react-router-dom";

export default function Sidemenu({ open, toggleDrawer }) {
  const navigate = useNavigate();

  const listOptions = () => (
    <Box
      role="presentation"
      onClick={toggleDrawer(false)}
    >

      //Lo que puede ver el administrador
      <List>
        <ListItemButton onClick={() => navigate("/home")}>
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText primary="Home" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/tarifas")}>
          <ListItemIcon>
          <InsertDriveFileIcon />
          </ListItemIcon>
          <ListItemText primary="Tarifas" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/karts")}>
          <ListItemIcon>
          <DirectionsCarIcon />
          </ListItemIcon>
          <ListItemText primary="Karts" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/reporte")}>
          <ListItemIcon>
          <BarChartIcon />
          </ListItemIcon>
          <ListItemText primary="Reporte" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/rack-semanal")}>
          <ListItemIcon>
          <CalendarTodayIcon />
          </ListItemIcon>
          <ListItemText primary="Rack Semanal" />
        </ListItemButton>

        // lo que puede ver el cliente

        <ListItemButton onClick={() => navigate("/home")}>
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText primary="Home" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/reserva")}>
          <ListItemIcon>
          <DateRangeIcon />
          </ListItemIcon>
          <ListItemText primary="Reserva" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/tarifas-cliente")}>
          <ListItemIcon>
          <InsertDriveFileIcon />
          </ListItemIcon>
          <ListItemText primary="Tarifas" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/rack-semanal")}>
          <ListItemIcon>
          <CalendarTodayIcon />
          </ListItemIcon>
          <ListItemText primary="Calendario Semanal" />
        </ListItemButton>
        
        

      </List>

    </Box>
  );

  return (
    <div>
      <Drawer anchor={"left"} open={open} onClose={toggleDrawer(false)}>
        {listOptions()}
      </Drawer>
    </div>
  );
}
