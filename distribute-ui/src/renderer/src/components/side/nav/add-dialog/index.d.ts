interface SearchResultItem {
  type: boolean
  id: number
}

interface Option {
  title: string
  work: (id: number) => void
}
