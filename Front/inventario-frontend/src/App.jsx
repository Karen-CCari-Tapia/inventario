import { useState } from 'react';
import axios from 'axios';
import { QrReader } from 'react-qr-reader';

export default function App() {
  const [qr, setQr] = useState('');
  const [precioVenta, setPrecioVenta] = useState('');
  const [modo, setModo] = useState('ingreso'); 
  const [mensaje, setMensaje] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensaje('');

    try {
      if (modo === 'ingreso') {
        const res = await axios.post('http://localhost:8080/api/productos/ingresoqr', {
          qr,
        });
        setMensaje(res.data);
      } else {
        const partes = qr.split(',');
        if (partes.length !== 3) {
          setMensaje('Formato QR inválido. Debe tener código, descripción, precio');
          return;
        }

        

        const res = await axios.post('http://localhost:8080/api/productos/salidaqr', {
          qr,
          precioVenta: parseFloat(precioVenta)
        });
        setMensaje(res.data);
      }
    } catch (error) {
      setMensaje('Error: ' + (error.response?.data || error.message));
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-4 bg-gray-100">
      <h1 className="text-2xl font-bold mb-4">Gestor de Inventario con QR</h1>

      <div className="mb-4">
        <button
          className={`px-4 py-2 rounded-l ${modo === 'ingreso' ? 'bg-blue-600 text-white' : 'bg-gray-300'}`}
          onClick={() => setModo('ingreso')}
        >
          Ingreso
        </button>
        <button
          className={`px-4 py-2 rounded-r ${modo === 'salida' ? 'bg-green-600 text-white' : 'bg-gray-300'}`}
          onClick={() => setModo('salida')}
        >
          Salida
        </button>
      </div>

      <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow-md w-full max-w-md">
        {modo === 'ingreso' && (
          <div className="mb-4">
            <QrReader
              constraints={{ facingMode: 'environment' }}
              onResult={(result) => {
                if (result?.text) {
                  setQr(result.text);
                }
              }}
              style={{ width: '100%' }}
            />
          </div>
        )}

        <label className="block mb-2 font-semibold">Código QR (formato: código, descripción, precio):</label>
        <input
          type="text"
          className="w-full p-2 border border-gray-300 rounded mb-4"
          value={qr}
          onChange={(e) => setQr(e.target.value)}
          required
        />

        {modo === 'salida' && (
          <>
            <label className="block mb-2 font-semibold">Precio de Venta:</label>
            <input
              type="number"
              step="0.01"
              className="w-full p-2 border border-gray-300 rounded mb-4"
              value={precioVenta}
              onChange={(e) => setPrecioVenta(e.target.value)}
              required
            />
          </>
        )}

        <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600">
          Guardar
        </button>

        {mensaje && <p className="mt-4 text-sm text-center text-red-700">{mensaje}</p>}
      </form>
    </div>
  );
}