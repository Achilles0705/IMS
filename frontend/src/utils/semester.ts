export function uniqueSemestersFrom<T>(items: T[], getter: (item: T) => string | undefined): string[] {
  const set = new Set<string>()
  items.forEach((item) => {
    const semester = getter(item)
    if (semester) {
      set.add(semester)
    }
  })
  return [...set].sort((a, b) => b.localeCompare(a))
}
