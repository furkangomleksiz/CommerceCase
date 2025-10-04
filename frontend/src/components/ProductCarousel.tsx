import { Swiper, SwiperSlide } from 'swiper/react'
import { Navigation, Pagination } from 'swiper/modules'
import { Product } from '../types/Product'
import ProductCard from './ProductCard'

import 'swiper/css'
import 'swiper/css/navigation'
import 'swiper/css/pagination'
import './ProductCarousel.css'

interface ProductCarouselProps {
  products: Product[]
}

const ProductCarousel = ({ products }: ProductCarouselProps) => {
  return (
    <div className="carousel-container">
      <Swiper
        modules={[Navigation, Pagination]}
        spaceBetween={24}
        slidesPerView={1}
        navigation={{
          prevEl: '.swiper-button-prev',
          nextEl: '.swiper-button-next',
        }}
        pagination={{ clickable: true }}
        breakpoints={{
          640: {
            slidesPerView: 2,
            spaceBetween: 20,
          },
          768: {
            slidesPerView: 3,
            spaceBetween: 24,
          },
          1024: {
            slidesPerView: 4,
            spaceBetween: 24,
          },
        }}
        className="product-swiper"
      >
        {products.map((product, index) => (
          <SwiperSlide key={index}>
            <ProductCard product={product} />
          </SwiperSlide>
        ))}
      </Swiper>
      
      <div className="swiper-button-prev custom-nav">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M15 18L9 12L15 6" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      </div>
      <div className="swiper-button-next custom-nav">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M9 18L15 12L9 6" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      </div>
    </div>
  )
}

export default ProductCarousel

