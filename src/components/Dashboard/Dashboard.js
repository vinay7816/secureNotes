import React, { useEffect, useState } from 'react';
import api from '../Api/api';

const formatHeader = (key) =>
  key
    .replace(/([a-z0-9])([A-Z])/g, '$1 $2')
    .replace(/[_-]/g, ' ')
    .replace(/^./, (character) => character.toUpperCase());

const isActionColumn = (key) =>
  ['action', 'actiontype', 'event', 'eventtype', 'operation', 'operationtype'].includes(
    key.replace(/[_-]/g, '').toLowerCase()
  );

const getActionStyle = (value) => {
  const action = String(value).toLowerCase();

  if (/delete|remove|revoke/.test(action)) {
    return 'bg-rose-50 text-rose-700 ring-rose-200';
  }
  if (/create|add|restore/.test(action)) {
    return 'bg-emerald-50 text-emerald-700 ring-emerald-200';
  }
  if (/update|edit|change/.test(action)) {
    return 'bg-sky-50 text-sky-700 ring-sky-200';
  }
  if (/view|read|access|login/.test(action)) {
    return 'bg-violet-50 text-violet-700 ring-violet-200';
  }

  return 'bg-slate-100 text-slate-700 ring-slate-200';
};

const formatValue = (value) => {
  if (value === null || value === undefined || value === '') {
    return '—';
  }

  if (typeof value === 'object') {
    return JSON.stringify(value);
  }

  return String(value);
};

const Dashboard = () => {
  const [records, setRecords] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchActivity = async () => {
      try {
        const response = await api.get('/audit/notes');
        console.log('Dashboard activity response:', response.data);
        if (!Array.isArray(response.data)) {
          throw new Error('The activity response must be an array.');
        }

        setRecords(
          response.data.filter(
            (record) => record && typeof record === 'object' && !Array.isArray(record)
          )
        );
      } catch (requestError) {
        console.error('Error fetching dashboard activity:', requestError);
        setError('Activity could not be loaded. Please try again later.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchActivity();
  }, []);

  const recordColumns = [...new Set(records.flatMap((record) => Object.keys(record)))];
  const actionColumns = recordColumns.filter(isActionColumn);
  const columns = [
    ...actionColumns,
    ...recordColumns.filter((column) => !actionColumns.includes(column))
  ];

  return (
    <main className="min-h-screen bg-gradient-to-br from-slate-50 via-white to-indigo-50/60 px-4 py-8 sm:px-6 lg:px-8">
      <section className="mx-auto max-w-7xl overflow-hidden rounded-2xl border border-slate-200/80 bg-white shadow-xl shadow-slate-200/50">
        <header className="flex flex-col gap-4 border-b border-slate-100 bg-gradient-to-r from-white to-indigo-50/50 px-6 py-6 sm:flex-row sm:items-center sm:justify-between sm:px-8">
          <div>
            <div className="flex items-center gap-3">
              <span className="flex h-11 w-11 items-center justify-center rounded-xl bg-indigo-600 text-lg font-semibold text-white shadow-lg shadow-indigo-200">
                ↗
              </span>
              <div>
                <p className="text-xs font-semibold uppercase tracking-[0.16em] text-indigo-600">
                  Overview
                </p>
                <h1 className="mt-1 text-2xl font-bold tracking-tight text-slate-900">
                  Activity log
                </h1>
              </div>
            </div>
            <p className="mt-3 text-sm text-slate-500">
              A clear record of what’s happening across your workspace.
            </p>
          </div>
          {!isLoading && !error && (
            <div className="flex w-fit items-center gap-2 rounded-full border border-slate-200 bg-white px-3.5 py-2 text-sm text-slate-600 shadow-sm">
              <span className="h-2 w-2 rounded-full bg-emerald-500" />
              <span className="font-semibold text-slate-900">{records.length}</span>
              {records.length === 1 ? 'activity' : 'activities'}
            </div>
          )}
        </header>

        <div className="overflow-x-auto">
          {isLoading ? (
            <p className="px-6 py-16 text-center text-sm text-slate-500">
              Loading activity…
            </p>
          ) : error ? (
            <p role="alert" className="px-6 py-16 text-center text-sm text-red-600">
              {error}
            </p>
          ) : records.length === 0 ? (
            <div className="px-6 py-16 text-center">
              <p className="text-base font-semibold text-slate-800">Nothing to see yet</p>
              <p className="mt-1 text-sm text-slate-500">New activity will appear here.</p>
            </div>
          ) : columns.length === 0 ? (
            <p className="px-6 py-16 text-center text-sm text-slate-500">
              Activity records do not contain any fields to display.
            </p>
          ) : (
            <table className="w-full min-w-max text-left text-sm">
              <thead className="bg-slate-50/90 text-xs uppercase tracking-wider text-slate-500">
                <tr>
                  {columns.map((column) => (
                    <th
                      key={column}
                      scope="col"
                      className="whitespace-nowrap px-6 py-4 font-semibold first:pl-8"
                    >
                      {formatHeader(column)}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100/80">
                {records.map((record, index) => (
                  <tr
                    key={record.id ?? record._id ?? index}
                    className="group transition-colors hover:bg-indigo-50/40"
                  >
                    {columns.map((column) => (
                      <td
                        key={column}
                        className={`max-w-xs whitespace-normal break-words px-6 py-4 text-slate-600 ${
                          isActionColumn(column) ? 'min-w-40 pl-8' : ''
                        }`}
                      >
                        {isActionColumn(column) && record[column] != null ? (
                          <span
                            className={`inline-flex items-center gap-2 rounded-full px-3 py-1.5 text-xs font-bold capitalize ring-1 ring-inset ${getActionStyle(record[column])}`}
                          >
                            <span className="h-1.5 w-1.5 rounded-full bg-current" />
                            {formatValue(record[column])}
                          </span>
                        ) : (
                          formatValue(record[column])
                        )}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </section>
    </main>
  );
};

export default Dashboard;
