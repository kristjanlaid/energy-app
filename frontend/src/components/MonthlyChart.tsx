import {
  Bar,
  BarChart,
  CartesianGrid,
  Legend,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

export type MonthlyData = {
  month: number;
  totalKwh: number;
  totalCost: number;
};

interface MonthlyChartProps {
  data: MonthlyData[];
}

const monthNames = [
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

function MonthlyChart({ data }: MonthlyChartProps) {
  const chartData = data.map((d) => ({
    name: monthNames[d.month - 1],
    kWh: d.totalKwh,
    cost: d.totalCost,
  }));

  return (
    <ResponsiveContainer width="100%" height={400}>
      <BarChart data={chartData}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Bar dataKey="kWh" fill="#8884d8" />
        <Bar dataKey="€" fill="#82ca9d" />
      </BarChart>
    </ResponsiveContainer>
  );
}

export default MonthlyChart;
