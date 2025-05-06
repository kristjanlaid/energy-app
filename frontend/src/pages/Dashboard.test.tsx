import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import Dashboard from './Dashboard';
import { BrowserRouter } from 'react-router-dom';
import { vi } from 'vitest';
import api from '../api/axios';

vi.mock('../api/axios');

describe('Dashboard Page', () => {
  beforeEach(() => {
    (api.get as any).mockImplementation((url: string) => {
      if (url === '/metering-points') {
        return Promise.resolve({
          data: [{ id: 'mock-id-1', name: 'Mock Location 1' }],
        });
      }
      if (url.startsWith('/consumption/cost/monthly')) {
        return Promise.resolve({
          data: [
            { month: 1, totalKwh: 100, totalCost: 20 },
            { month: 2, totalKwh: 150, totalCost: 25 },
          ],
        });
      }
      return Promise.reject(new Error('Unknown endpoint'));
    });
  });

  test('renders dropdowns and chart title', async () => {
    render(
      <BrowserRouter>
        <Dashboard />
      </BrowserRouter>
    );

    // Wait for metering points to load
    expect(await screen.findByText('Monthly Energy Overview')).toBeInTheDocument();
    expect(await screen.findByLabelText('Location')).toBeInTheDocument();
    expect(await screen.findByLabelText('Year')).toBeInTheDocument();
    expect(await screen.findByText(/Monthly Breakdown/)).toBeInTheDocument();
  });

  test('logs out and clears token', async () => {
    // Mock window.location
    delete window.location;
    window.location = { href: '', assign: vi.fn() } as any;

    render(
      <BrowserRouter>
        <Dashboard />
      </BrowserRouter>
    );

    const logoutButton = screen.getByRole('button', { name: /logout/i });
    fireEvent.click(logoutButton);

    await waitFor(() => {
      expect(localStorage.getItem('token')).toBe(null);
      expect(window.location.href).toBe('/');
    });
  });
});
