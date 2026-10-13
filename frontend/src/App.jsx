import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {
  Users, DollarSign, Building2, Globe, Search,
  ChevronLeft, ChevronRight, TrendingUp, Filter
} from 'lucide-react';

const API_BASE_URL = 'http://localhost:8085/api/v1/employees';

export default function App() {
  const [analytics, setAnalytics] = useState(null);
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);

  // Filter & Pagination States
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [search, setSearch] = useState('');
  const [country, setCountry] = useState('');
  const [department, setDepartment] = useState('');

  // Fetch Analytics Summary
  useEffect(() => {
    axios.get(`${API_BASE_URL}/analytics/summary`)
        .then(res => setAnalytics(res.data))
        .catch(err => console.error("Error loading analytics:", err));
  }, []);

  // Fetch Employee Directory
  useEffect(() => {
    setLoading(true);
    axios.get(API_BASE_URL, {
      params: { page, size: 10, search, country, department }
    })
        .then(res => {
          setEmployees(res.data.content);
          setTotalPages(res.data.totalPages);
          setTotalElements(res.data.totalElements);
          setLoading(false);
        })
        .catch(err => {
          console.error("Error loading employees:", err);
          setLoading(false);
        });
  }, [page, search, country, department]);

  return (
      <div className="min-h-screen bg-slate-50 text-slate-900 p-6">
        <div className="max-w-7xl mx-auto space-y-6">

          {/* Header */}
          <div className="flex justify-between items-center bg-white p-6 rounded-xl shadow-sm border border-slate-200">
            <div>
              <h1 className="text-2xl font-bold text-slate-800">ACME Org — HR Salary Portal</h1>
              <p className="text-sm text-slate-500">Real-time Compensation Analytics & Directory (10,000 Employees)</p>
            </div>
            <span className="px-3 py-1 bg-emerald-100 text-emerald-800 rounded-full text-xs font-semibold">
            System Live
          </span>
          </div>

          {/* Analytics Cards */}
          {analytics && (
              <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm flex items-center justify-between">
                  <div>
                    <p className="text-xs font-medium text-slate-500 uppercase">Total Employees</p>
                    <p className="text-2xl font-bold text-slate-800 mt-1">{analytics.totalEmployees.toLocaleString()}</p>
                  </div>
                  <div className="p-3 bg-blue-50 text-blue-600 rounded-lg"><Users className="w-6 h-6"/></div>
                </div>

                <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm flex items-center justify-between">
                  <div>
                    <p className="text-xs font-medium text-slate-500 uppercase">Total Payroll</p>
                    <p className="text-xl font-bold text-slate-800 mt-1">₹{Number(analytics.totalPayroll).toLocaleString()}</p>
                  </div>
                  <div className="p-3 bg-emerald-50 text-emerald-600 rounded-lg"><DollarSign className="w-6 h-6"/></div>
                </div>

                <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm flex items-center justify-between">
                  <div>
                    <p className="text-xs font-medium text-slate-500 uppercase">Average Salary</p>
                    <p className="text-xl font-bold text-slate-800 mt-1">₹{Number(analytics.averageSalary).toLocaleString()}</p>
                  </div>
                  <div className="p-3 bg-indigo-50 text-indigo-600 rounded-lg"><TrendingUp className="w-6 h-6"/></div>
                </div>

                <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm flex items-center justify-between">
                  <div>
                    <p className="text-xs font-medium text-slate-500 uppercase">Active Global Regions</p>
                    <p className="text-2xl font-bold text-slate-800 mt-1">{Object.keys(analytics.countByCountry).length}</p>
                  </div>
                  <div className="p-3 bg-purple-50 text-purple-600 rounded-lg"><Globe className="w-6 h-6"/></div>
                </div>
              </div>
          )}

          {/* Controls: Search & Multi-Filters */}
          <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-sm flex flex-col md:flex-row gap-4 justify-between items-center">
            <div className="relative w-full md:w-96">
              <Search className="absolute left-3 top-2.5 w-4 h-4 text-slate-400" />
              <input
                  type="text"
                  placeholder="Search by name, code or email..."
                  value={search}
                  onChange={(e) => { setSearch(e.target.value); setPage(0); }}
                  className="w-full pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="flex gap-3 w-full md:w-auto">
              <select
                  value={country}
                  onChange={(e) => { setCountry(e.target.value); setPage(0); }}
                  className="px-3 py-2 border border-slate-200 rounded-lg text-sm bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">All Countries</option>
                <option value="India">India</option>
                <option value="United States">United States</option>
                <option value="United Kingdom">United Kingdom</option>
                <option value="Germany">Germany</option>
                <option value="Japan">Japan</option>
                <option value="Singapore">Singapore</option>
              </select>

              <select
                  value={department}
                  onChange={(e) => { setDepartment(e.target.value); setPage(0); }}
                  className="px-3 py-2 border border-slate-200 rounded-lg text-sm bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">All Departments</option>
                <option value="Engineering">Engineering</option>
                <option value="Product">Product</option>
                <option value="Sales">Sales</option>
                <option value="Human Resources">Human Resources</option>
                <option value="Finance">Finance</option>
                <option value="Marketing">Marketing</option>
                <option value="Legal">Legal</option>
              </select>
            </div>
          </div>

          {/* Employee Data Grid */}
          <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
            <table className="w-full text-left text-sm text-slate-600">
              <thead className="bg-slate-50 border-b border-slate-200 text-xs uppercase text-slate-500 font-semibold">
              <tr>
                <th className="p-4">Code</th>
                <th className="p-4">Employee</th>
                <th className="p-4">Department & Role</th>
                <th className="p-4">Location</th>
                <th className="p-4 text-right">Base Salary</th>
                <th className="p-4 text-right">Bonus</th>
              </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
              {loading ? (
                  <tr>
                    <td colSpan="6" className="p-8 text-center text-slate-400">Loading records...</td>
                  </tr>
              ) : employees.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="p-8 text-center text-slate-400">No matching employees found.</td>
                  </tr>
              ) : (
                  employees.map((emp) => (
                      <tr key={emp.id} className="hover:bg-slate-50">
                        <td className="p-4 font-mono text-xs font-bold text-slate-700">{emp.employeeCode}</td>
                        <td className="p-4">
                          <p className="font-semibold text-slate-800">{emp.firstName} {emp.lastName}</p>
                          <p className="text-xs text-slate-400">{emp.email}</p>
                        </td>
                        <td className="p-4">
                          <p className="text-slate-800">{emp.jobTitle}</p>
                          <p className="text-xs text-slate-400">{emp.department}</p>
                        </td>
                        <td className="p-4">
                      <span className="px-2 py-1 bg-slate-100 text-slate-700 rounded text-xs font-medium">
                        {emp.country} ({emp.currency})
                      </span>
                        </td>
                        <td className="p-4 text-right font-medium text-slate-800">
                          {emp.currency} {Number(emp.baseSalary).toLocaleString()}
                        </td>
                        <td className="p-4 text-right text-emerald-600 font-medium">
                          + {emp.currency} {Number(emp.bonus).toLocaleString()}
                        </td>
                      </tr>
                  ))
              )}
              </tbody>
            </table>

            {/* Pagination Footer */}
            <div className="p-4 bg-slate-50 border-t border-slate-200 flex justify-between items-center text-xs text-slate-500">
              <p>Showing {employees.length} of {totalElements.toLocaleString()} employees</p>
              <div className="flex gap-2 items-center">
                <button
                    disabled={page === 0}
                    onClick={() => setPage(p => p - 1)}
                    className="p-1 border rounded bg-white disabled:opacity-40 hover:bg-slate-100"
                >
                  <ChevronLeft className="w-4 h-4"/>
                </button>
                <span>Page {page + 1} of {totalPages}</span>
                <button
                    disabled={page + 1 >= totalPages}
                    onClick={() => setPage(p => p + 1)}
                    className="p-1 border rounded bg-white disabled:opacity-40 hover:bg-slate-100"
                >
                  <ChevronRight className="w-4 h-4"/>
                </button>
              </div>
            </div>
          </div>

        </div>
      </div>
  );
}