import './globals.css'
import 'react-toastify/ReactToastify.css'
import type { Metadata } from 'next'
import { Roboto } from 'next/font/google'

const roboto = Roboto({ subsets: ['latin'], weight: "300" })

export const metadata: Metadata = {
  title: 'ImageLite App',
  description: 'Aplicação do Curso de React - Controle de Imagens',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={roboto.className}>{children}</body>
    </html>
  )
}
