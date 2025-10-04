import { useState, useEffect } from 'react'
import ProductCarousel from './components/ProductCarousel'
import { Product } from './types/Product'
import axios from 'axios'
import './App.css'

function App() {
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetchProducts()
  }, [])

  const fetchProducts = async () => {
    try {
      setLoading(true)
      const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080'
      const response = await axios.get(`${apiUrl}/api/products`)
      setProducts(response.data)
      setError(null)
    } catch (err) {
      setError('Failed to load products. Please make sure the backend is running.')
      console.error('Error fetching products:', err)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <div className="container">
        <div className="loading">Loading products...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="container">
        <div className="error">{error}</div>
      </div>
    )
  }

  return (
    <div className="container">
      <header className="header">
        <h1 className="title">Product List</h1>
      </header>
      <ProductCarousel products={products} />
    </div>
  )
}

export default App

