import { useEffect, useState } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Container,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Grid,
  Paper,
  IconButton,
} from "@mui/material";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import api from "../api/axios";
import LogoutIcon from "@mui/icons-material/Logout";

const monthLabels = [
  "Jan",
  "Feb",
  "Mar",
  "Apr",
  "May",
  "Jun",
  "Jul",
  "Aug",
  "Sep",
  "Oct",
  "Nov",
  "Dec",
];

function Dashboard() {
  const [meteringPoints, setMeteringPoints] = useState<any[]>([]);
  const [selectedPoint, setSelectedPoint] = useState("");
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
  const [monthlyData, setMonthlyData] = useState<any[]>([]);
  const [error, setError] = useState("");

  const handleLogout = () => {
    localStorage.removeItem("token");
    window.location.href = "/";
  };

  useEffect(() => {
    api
      .get("/metering-points")
      .then((res) => {
        setMeteringPoints(res.data);
        if (res.data.length > 0) {
          setSelectedPoint(res.data[0].id);
        }
      })
      .catch(() => setError("Failed to load metering points"));
  }, []);

  useEffect(() => {
    if (selectedPoint && selectedYear) {
      api
        .get(`/consumption/cost/monthly?meteringPointId=${selectedPoint}`)
        .then((res) => {
          const dataMap: Record<number, any> = {};
          res.data.forEach((entry: any) => {
            dataMap[entry.month] = {
              month: monthLabels[entry.month - 1],
              totalKwh: entry.totalKwh,
              totalCost: entry.totalCost,
            };
          });

          const fullData = monthLabels.map((label, index) => ({
            month: label,
            totalKwh: dataMap[index + 1]?.totalKwh || 0,
            totalCost: dataMap[index + 1]?.totalCost || 0,
          }));

          setMonthlyData(fullData);
        })
        .catch(() => setError("Failed to load data"));
    }
  }, [selectedPoint, selectedYear]);

  return (
    <>
      {/* Navbar */}
      <AppBar position="static" sx={{ backgroundColor: "#007f3d" }}>
        <Toolbar sx={{ justifyContent: "space-between" }}>
          <Typography variant="h6" component="div" sx={{ fontWeight: "bold" }}>
            Energy Dashboard
          </Typography>
          <IconButton
          aria-label="logout"
            sx={{
              backgroundColor: "#fff",
              color: "#007f3d",
              "&:hover": {
                backgroundColor: "#e0e0e0",
              },
            }}
            onClick={handleLogout}
          >
            <LogoutIcon />
          </IconButton>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Monthly Energy Overview
        </Typography>

        {/* Dropdowns */}
        <Grid container spacing={2} sx={{ mb: 3 }}>
          <Grid item xs={12} md={6}>
            <FormControl fullWidth>
              <InputLabel id="location-label">Location</InputLabel>
              <Select
                labelId="location-label"
                id="location-select"
                value={selectedPoint}
                label="Metering Point"
                onChange={(e) => setSelectedPoint(e.target.value)}
              >
                {meteringPoints.map((point) => (
                  <MenuItem key={point.id} value={point.id}>
                    {point.name || point.id}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>

          <Grid item xs={12} md={6}>
            <FormControl fullWidth>
              <InputLabel id="year-label">Year</InputLabel>
              <Select
                labelId="year-label"
                id="year-select"
                value={selectedYear}
                label="Year"
                onChange={(e) => setSelectedYear(e.target.value)}
              >
                {[2022, 2023, 2024, 2025].map((year) => (
                  <MenuItem key={year} value={year}>
                    {year}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
        </Grid>

        {error && <Typography color="error">{error}</Typography>}

        {/* Chart */}
        <Paper elevation={3} sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Monthly Breakdown – {selectedYear}
          </Typography>

          <ResponsiveContainer width="100%" height={350}>
            <BarChart data={monthlyData}>
              <XAxis dataKey="month" />
              <YAxis yAxisId="left" orientation="left" stroke="#1976d2" />
              <YAxis yAxisId="right" orientation="right" stroke="#9c27b0" />
              <Tooltip />
              <Legend />
              <Bar
                yAxisId="left"
                dataKey="totalCost"
                fill="#1976d2"
                name="Cost (€)"
                animationDuration={800}
              />
              <Bar
                yAxisId="right"
                dataKey="totalKwh"
                fill="#9c27b0"
                name="Consumption (kWh)"
                animationDuration={1000}
              />
            </BarChart>
          </ResponsiveContainer>
        </Paper>
      </Container>
    </>
  );
}

export default Dashboard;
