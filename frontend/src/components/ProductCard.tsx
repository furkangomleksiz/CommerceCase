import { useState } from 'react'
import { Product, ColorType } from '../types/Product'
import './ProductCard.css'

interface ProductCardProps {
  product: Product
}

const ProductCard = ({ product }: ProductCardProps) => {
  const [selectedColor, setSelectedColor] = useState<ColorType>('yellow')

  const colorLabels: Record<ColorType, string> = {
    yellow: 'Yellow Gold',
    white: 'White Gold',
    rose: 'Rose Gold'
  }

  const colorHexCodes: Record<ColorType, string> = {
    yellow: '#E6CA97',
    white: '#D9D9D9',
    rose: '#E1A4A9'
  }

  return (
    <div className="product-card">
      <div className="product-image-container">
        <img
          src={product.images[selectedColor]}
          alt={product.name}
          className="product-image"
        />
      </div>
      
      <div className="product-info">
        <h3 className="product-title">{product.name}</h3>
        <p className="product-price">${product.price.toFixed(2)} USD</p>
        
        <div className="color-selector">
          {(['yellow', 'white', 'rose'] as ColorType[]).map((color) => (
            <button
              key={color}
              className={`color-swatch ${selectedColor === color ? 'active' : ''}`}
              style={{ backgroundColor: colorHexCodes[color] }}
              onClick={() => setSelectedColor(color)}
              aria-label={colorLabels[color]}
            />
          ))}
        </div>
        
        <p className="color-label">{colorLabels[selectedColor]}</p>
        
        <div className="rating">
          {[...Array(5)].map((_, index) => (
            <span
              key={index}
              className={`star ${index < Math.floor(product.rating) ? 'filled' : ''} ${
                index < product.rating && index >= Math.floor(product.rating) ? 'half' : ''
              }`}
            >
              ★
            </span>
          ))}
          <span className="rating-value">{product.rating}/5</span>
        </div>
      </div>
    </div>
  )
}

export default ProductCard

